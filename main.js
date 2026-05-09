/* ══════════════════════════════════════════════════════════
   script.js  —  Round Robin vs SRTF Simulator
   ══════════════════════════════════════════════════════════ */

// ── Process colors (one per process index)
const COLORS = [
  '#3b82f6','#f97316','#22c55e','#f59e0b',
  '#ec4899','#14b8a6','#8b5cf6','#ef4444','#06b6d4','#84cc16'
];

// ── Shorthand for getElementById
function $(id) { return document.getElementById(id); }

/* ══════════════════════════════════════════════════════════
   ALERTS
══════════════════════════════════════════════════════════ */
function showAlert(msg, type = 'info') {
  const wrap = $('alerts');
  const div = document.createElement('div');
  div.className = `alert alert-${type}`;
  const icon = type === 'error' ? '❌' : type === 'success' ? '✅' : 'ℹ️';
  div.innerHTML = `<span>${icon}</span><span>${msg}</span>`;
  wrap.appendChild(div);
  setTimeout(() => div.remove(), 5000);
}

function clearAlerts() { $('alerts').innerHTML = ''; }

/* ══════════════════════════════════════════════════════════
   BUILD INPUT TABLE
   Called when the user changes "Number of Processes"
══════════════════════════════════════════════════════════ */
function buildTable() {
  const n = parseInt($('numProc').value) || 1;
  const tbody = $('proc-body');
  const current = tbody.querySelectorAll('tr').length;

  // Add missing rows
  for (let i = current; i < n; i++) {
    const tr = document.createElement('tr');
    tr.id = `row-${i}`;
    tr.innerHTML = `
      <td><span class="pid-label">P${i + 1}</span></td>
      <td><input type="number" id="at-${i}" min="0" value="${i}" /></td>
      <td><input type="number" id="bt-${i}" min="1" value="${Math.floor(Math.random() * 8) + 2}" /></td>
      <td><input type="number" id="pr-${i}" min="1" value="${i + 1}" /></td>
    `;
    tbody.appendChild(tr);
  }

  // Remove extra rows
  for (let i = n; i < current; i++) {
    const r = $(`row-${i}`);
    if (r) r.remove();
  }
}

/* ══════════════════════════════════════════════════════════
   QUICK SCENARIOS
   Each scenario pre-fills the table with interesting data
══════════════════════════════════════════════════════════ */
const SCENARIOS = {
  // A: Normal mixed workload — good default
  A: { q: 3, procs: [{at:0,bt:5},{at:1,bt:3},{at:2,bt:8},{at:3,bt:6}] },
  // B: Quantum = 1 — very fine-grained RR, shows context switching
  B: { q: 1, procs: [{at:0,bt:6},{at:0,bt:4},{at:0,bt:2},{at:0,bt:3}] },
  // C: Many short jobs — SRTF dominates
  C: { q: 4, procs: [{at:0,bt:1},{at:1,bt:2},{at:2,bt:1},{at:3,bt:3},{at:4,bt:2},{at:5,bt:1}] },
  // D: All equal — RR shows perfect fairness
  D: { q: 2, procs: [{at:0,bt:4},{at:0,bt:4},{at:0,bt:4},{at:0,bt:4}] },
  // E: Invalid burst time — tests validation
  E: { q: -1, procs: [{at:0,bt:5},{at:1,bt:-3},{at:2,bt:4}] }
};

function loadScenario(key) {
  clearAlerts();
  const sc = SCENARIOS[key];
  $('numProc').value = sc.procs.length;
  $('quantum').value = sc.q;
  buildTable();

  sc.procs.forEach((p, i) => {
    if ($(`at-${i}`)) $(`at-${i}`).value = p.at;
    if ($(`bt-${i}`)) $(`bt-${i}`).value = p.bt;
  });

  if (key === 'E') showAlert('Scenario E: P2 has invalid burst time (–3). Validator will catch it.', 'info');
  if (key === 'B') showAlert('Scenario B: Quantum = 1. Watch fine-grained RR switching.', 'info');
  if (key === 'C') showAlert('Scenario C: Many short jobs. SRTF should outperform significantly.', 'info');
}

/* ══════════════════════════════════════════════════════════
   VALIDATION
   Returns array of error messages; empty = valid
══════════════════════════════════════════════════════════ */
function validate(procs, q) {
  const errors = [];

  if (!Number.isInteger(q) || q < 1)
    errors.push('Time Quantum must be a positive integer ≥ 1');

  procs.forEach((p, i) => {
    if (!Number.isFinite(p.at) || p.at < 0)
      errors.push(`P${i+1}: Arrival Time must be ≥ 0`);
    if (!Number.isFinite(p.bt) || p.bt < 1)
      errors.push(`P${i+1}: Burst Time must be a positive integer ≥ 1`);
  });

  return errors;
}

/* ══════════════════════════════════════════════════════════
   ROUND ROBIN ALGORITHM
   ──────────────────────────────────────────────────────────
   Steps:
   1. Sort processes by arrival time
   2. Maintain a circular ready queue
   3. Each process runs for min(quantum, remaining_time)
   4. Newly arrived processes join the queue at each step
   5. Record every CPU segment for the Gantt chart
══════════════════════════════════════════════════════════ */
function simulateRR(procs, quantum) {
  const n = procs.length;
  const remaining = procs.map(p => p.bt);   // remaining burst times
  const ct = new Array(n).fill(0);           // completion times
  const rt = new Array(n).fill(-1);          // first response times
  const gantt = [];                          // [{pid, start, end}]
  const readyQueue = [];
  let time = 0;
  let done = 0;

  // Sort process indices by arrival time
  const sorted = procs.map((_, i) => i).sort((a, b) => procs[a].at - procs[b].at);
  let next = 0; // pointer into sorted[]

  // Enqueue all processes that arrive at time 0
  while (next < n && procs[sorted[next]].at <= time)
    readyQueue.push(sorted[next++]);

  // Save snapshot of the initial ready queue for display
  const firstSnapshot = [...readyQueue];

  while (done < n) {
    // If no process is ready, jump time to next arrival
    if (readyQueue.length === 0) {
      if (next < n) {
        time = procs[sorted[next]].at;
        while (next < n && procs[sorted[next]].at <= time)
          readyQueue.push(sorted[next++]);
      } else break;
    }

    const idx = readyQueue.shift();          // dequeue front

    // Record first response
    if (rt[idx] === -1) rt[idx] = time;

    // Run for quantum or remaining, whichever is smaller
    const exec = Math.min(quantum, remaining[idx]);
    gantt.push({ pid: idx, start: time, end: time + exec });
    time += exec;
    remaining[idx] -= exec;

    // Enqueue any processes that arrived during this slice
    while (next < n && procs[sorted[next]].at <= time)
      readyQueue.push(sorted[next++]);

    // If process is not done, push it back to the end of the queue
    if (remaining[idx] > 0) {
      readyQueue.push(idx);
    } else {
      ct[idx] = time;
      done++;
    }
  }

  return { gantt, ct, rt, firstSnapshot };
}

/* ══════════════════════════════════════════════════════════
   SRTF ALGORITHM (Shortest Remaining Time First)
   ──────────────────────────────────────────────────────────
   Steps:
   1. At every time unit, look at all arrived processes
   2. Pick the one with the smallest remaining burst time
   3. Run it for 1 unit (preemptive — any shorter job interrupts)
   4. Merge consecutive segments of the same process in Gantt
══════════════════════════════════════════════════════════ */
function simulateSRTF(procs) {
  const n = procs.length;
  const remaining = procs.map(p => p.bt);
  const ct = new Array(n).fill(0);
  const rt = new Array(n).fill(-1);
  const gantt = [];
  let time = 0;
  let done = 0;

  while (done < n) {
    // Find the arrived process with the shortest remaining time
    let best = -1, minR = Infinity;
    for (let i = 0; i < n; i++) {
      if (procs[i].at <= time && remaining[i] > 0) {
        if (remaining[i] < minR ||
           (remaining[i] === minR && procs[i].at < procs[best].at)) {
          minR = remaining[i];
          best = i;
        }
      }
    }

    // No process available — CPU is idle, advance time
    if (best === -1) { time++; continue; }

    // Record first response
    if (rt[best] === -1) rt[best] = time;

    // Extend last gantt block if same process continues
    if (gantt.length > 0 && gantt[gantt.length - 1].pid === best) {
      gantt[gantt.length - 1].end++;
    } else {
      gantt.push({ pid: best, start: time, end: time + 1 });
    }

    time++;
    remaining[best]--;

    if (remaining[best] === 0) {
      ct[best] = time;
      done++;
    }
  }

  return { gantt, ct, rt };
}

/* ══════════════════════════════════════════════════════════
   RENDER GANTT CHART
══════════════════════════════════════════════════════════ */
function renderGantt(ganttId, axisId, legendId, gantt, procs) {
  const gc = $(ganttId), ax = $(axisId);
  gc.innerHTML = '';
  ax.innerHTML = '';

  if (!gantt.length) {
    gc.innerHTML = '<span style="color:var(--muted);padding:12px">No data</span>';
    return;
  }

  const totalTime = gantt[gantt.length - 1].end;
  // Scale: at least 36px per unit, at most 72px; shrink for long simulations
  const scale = Math.max(36, Math.min(72, 900 / totalTime));

  // Legend
  const pids = [...new Set(gantt.map(g => g.pid))];
  $(legendId).innerHTML = pids.map(pid => `
    <div class="legend-item">
      <div class="legend-dot" style="background:${COLORS[pid % COLORS.length]}"></div>
      <span>P${pid + 1}</span>
    </div>`).join('');

  // Gantt blocks
  gantt.forEach(g => {
    const w = (g.end - g.start) * scale;
    const div = document.createElement('div');
    div.className = 'g-block';
    div.style.width = w + 'px';
    div.style.background = COLORS[g.pid % COLORS.length];
    div.textContent = `P${g.pid + 1}`;
    div.title = `P${g.pid + 1} | [${g.start} → ${g.end}] | duration = ${g.end - g.start}`;
    gc.appendChild(div);
  });

  // Time axis ticks
  const times = [...new Set(gantt.flatMap(g => [g.start, g.end]))].sort((a, b) => a - b);
  times.forEach((t, i) => {
    const tick = document.createElement('div');
    tick.className = 'g-tick';
    if (i === 0) {
      tick.style.width = '0px';
    } else {
      tick.style.width = `${(t - times[i - 1]) * scale}px`;
    }
    tick.textContent = t;
    ax.appendChild(tick);
  });
}

/* ══════════════════════════════════════════════════════════
   RENDER METRICS TABLE
   Returns average WT, TAT, RT for comparison
══════════════════════════════════════════════════════════ */
function renderTable(tbodyId, procs, ct, rt) {
  const tbody = $(tbodyId);
  tbody.innerHTML = '';
  let sumWT = 0, sumTAT = 0, sumRT = 0;

  procs.forEach((p, i) => {
    const tat = ct[i] - p.at;
    const wt  = tat - p.bt;
    const rsp = rt[i] - p.at;
    sumWT += wt; sumTAT += tat; sumRT += rsp;

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td style="color:${COLORS[i % COLORS.length]};font-weight:700">P${i + 1}</td>
      <td>${p.at}</td><td>${p.bt}</td><td>${ct[i]}</td>
      <td>${tat}</td><td>${wt}</td><td>${rsp}</td>`;
    tbody.appendChild(tr);
  });

  // Averages row
  const n = procs.length;
  const avg = tr => document.createElement('tr');
  const avgRow = document.createElement('tr');
  avgRow.className = 'avg-row';
  avgRow.innerHTML = `
    <td colspan="4">Averages</td>
    <td>${r2(sumTAT / n)}</td>
    <td>${r2(sumWT / n)}</td>
    <td>${r2(sumRT / n)}</td>`;
  tbody.appendChild(avgRow);

  return { avgWT: sumWT / n, avgTAT: sumTAT / n, avgRT: sumRT / n };
}

/* ══════════════════════════════════════════════════════════
   COMPARISON BARS & WINNER BADGES
══════════════════════════════════════════════════════════ */
function r2(n) { return Math.round(n * 100) / 100; }

function setBar(fillId, valId, val, maxVal) {
  const pct = maxVal > 0 ? Math.min(100, (val / maxVal) * 100) : 0;
  $(fillId).style.width = pct + '%';
  $(valId).textContent = r2(val);
}

function setWinner(spanId, rrVal, srtfVal) {
  const span = $(spanId);
  if (rrVal === srtfVal) {
    span.className = 'winner winner-tie'; span.textContent = 'Tie';
  } else if (rrVal < srtfVal) {
    span.className = 'winner winner-rr'; span.textContent = 'RR ✓';
  } else {
    span.className = 'winner winner-srtf'; span.textContent = 'SRTF ✓';
  }
}

/* ══════════════════════════════════════════════════════════
   MAIN — RUN SIMULATION
   Called when "Run Simulation" is clicked
══════════════════════════════════════════════════════════ */
function runSimulation() {
  clearAlerts();

  // 1. Read inputs
  const n = parseInt($('numProc').value) || 0;
  const q = parseInt($('quantum').value);
  const procs = [];

  for (let i = 0; i < n; i++) {
    procs.push({
      at: parseFloat($(`at-${i}`).value),
      bt: parseFloat($(`bt-${i}`).value),
      pr: parseFloat($(`pr-${i}`).value) || i + 1
    });
  }

  // 2. Validate
  const errors = validate(procs, q);
  if (errors.length) {
    errors.forEach(e => showAlert(e, 'error'));
    return;
  }

  // 3. Round Robin simulation
  const rrRes = simulateRR(procs, q);
  renderGantt('rr-gantt', 'rr-axis', 'rr-legend', rrRes.gantt, procs);
  const rrM = renderTable('rr-tbody', procs, rrRes.ct, rrRes.rt);
  $('rr-q-label').textContent = `Q = ${q}`;

  // Show ready queue snapshot
  const qbox = $('queue-box');
  const qchips = $('queue-chips');
  qbox.style.display = 'block';
  qchips.innerHTML = rrRes.firstSnapshot.length
    ? rrRes.firstSnapshot.map(i => `<span class="chip">P${i + 1}</span>`).join('')
    : '<span style="color:var(--muted)">Empty at time 0</span>';

  // 4. SRTF simulation
  const srtfRes = simulateSRTF(procs);
  renderGantt('srtf-gantt', 'srtf-axis', 'srtf-legend', srtfRes.gantt, procs);
  const srtfM = renderTable('srtf-tbody', procs, srtfRes.ct, srtfRes.rt);

  // 5. Comparison bars
  const maxWT  = Math.max(rrM.avgWT,  srtfM.avgWT,  0.001);
  const maxTAT = Math.max(rrM.avgTAT, srtfM.avgTAT, 0.001);
  const maxRT  = Math.max(rrM.avgRT,  srtfM.avgRT,  0.001);

  setBar('bar-rr-wt',   'val-rr-wt',   rrM.avgWT,   maxWT);
  setBar('bar-srtf-wt', 'val-srtf-wt', srtfM.avgWT, maxWT);
  setBar('bar-rr-tat',  'val-rr-tat',  rrM.avgTAT,  maxTAT);
  setBar('bar-srtf-tat','val-srtf-tat',srtfM.avgTAT,maxTAT);
  setBar('bar-rr-rt',   'val-rr-rt',   rrM.avgRT,   maxRT);
  setBar('bar-srtf-rt', 'val-srtf-rt', srtfM.avgRT, maxRT);

  const rrTP   = r2(n / rrRes.gantt[rrRes.gantt.length - 1].end);
  const srtfTP = r2(n / srtfRes.gantt[srtfRes.gantt.length - 1].end);
  const maxTP  = Math.max(rrTP, srtfTP, 0.001);
  setBar('bar-rr-tp',   'val-rr-tp',   rrTP,   maxTP);
  setBar('bar-srtf-tp', 'val-srtf-tp', srtfTP, maxTP);

  setWinner('wt-badge',  rrM.avgWT,  srtfM.avgWT);
  setWinner('tat-badge', rrM.avgTAT, srtfM.avgTAT);
  setWinner('rt-badge',  rrM.avgRT,  srtfM.avgRT);

  // 6. Determine winners for conclusion text
  const wtW  = rrM.avgWT  <= srtfM.avgWT  ? 'Round Robin' : 'SRTF';
  const tatW = rrM.avgTAT <= srtfM.avgTAT ? 'Round Robin' : 'SRTF';
  const rtW  = rrM.avgRT  <= srtfM.avgRT  ? 'Round Robin' : 'SRTF';
  const recommend = srtfM.avgWT < rrM.avgWT ? 'SRTF' : 'Round Robin';

  // 7. Analysis summary
  $('analysis-box').innerHTML = `
    <p>With <strong>${n} processes</strong> and <strong>Time Quantum = ${q}</strong>:</p>
    <br>
    <p>✅ <strong>Avg Waiting Time:</strong>
       RR = ${r2(rrM.avgWT)}, SRTF = ${r2(srtfM.avgWT)}
       → <strong>${wtW}</strong> wins</p>
    <p>✅ <strong>Avg Turnaround Time:</strong>
       RR = ${r2(rrM.avgTAT)}, SRTF = ${r2(srtfM.avgTAT)}
       → <strong>${tatW}</strong> wins</p>
    <p>✅ <strong>Avg Response Time:</strong>
       RR = ${r2(rrM.avgRT)}, SRTF = ${r2(srtfM.avgRT)}
       → <strong>${rtW}</strong> wins</p>
    <br>
    <p>⚖️ <strong>Fairness:</strong>
       Round Robin gives each process equal CPU slices — no starvation.
       SRTF can starve long processes if short ones keep arriving.</p>
    <p>⚡ <strong>Efficiency:</strong>
       SRTF minimizes waiting and turnaround by always picking the shortest job.</p>
    <p>🔄 <strong>Preemption:</strong>
       SRTF preempts immediately when a shorter job arrives.
       RR only preempts at quantum boundaries (every ${q} units).</p>
    <p>⚙️ <strong>Quantum Effect (Q = ${q}):</strong>
       ${q <= 2
         ? 'Small quantum → many context switches, better fairness and response time.'
         : q >= 8
           ? 'Large quantum → few context switches, behavior approaches FCFS.'
           : 'Moderate quantum → balanced trade-off between fairness and overhead.'}</p>
    <br>
    <p>📌 <strong>Recommendation:</strong>
       Use <strong>${recommend}</strong> for this workload.
       ${recommend === 'SRTF'
         ? 'SRTF gives lower waiting and turnaround times (efficiency-critical).'
         : 'Round Robin gives better fairness and response time (interactive systems).'}</p>
  `;

  // 8. Q&A section
  $('qa-content').innerHTML = `
    <p><strong>Q1 — Which algorithm gave better average waiting time?</strong><br>
    → ${wtW} &nbsp;(RR: ${r2(rrM.avgWT)}, SRTF: ${r2(srtfM.avgWT)})</p>
    <br>
    <p><strong>Q2 — Which algorithm gave better average response time?</strong><br>
    → ${rtW} &nbsp;(RR: ${r2(rrM.avgRT)}, SRTF: ${r2(srtfM.avgRT)})</p>
    <br>
    <p><strong>Q3 — Did Round Robin appear fairer?</strong><br>
    → Yes. Every process gets equal CPU slices regardless of burst time.
       SRTF can indefinitely delay long processes.</p>
    <br>
    <p><strong>Q4 — Did SRTF complete short jobs faster?</strong><br>
    → Yes. SRTF always selects the process with the least remaining time,
       so short jobs finish almost immediately.</p>
    <br>
    <p><strong>Q5 — How did Quantum = ${q} affect Round Robin?</strong><br>
    → ${q <= 2
        ? `Small quantum (${q}): High context-switch overhead, but excellent fairness and response time.`
        : q >= 8
          ? `Large quantum (${q}): Very few context switches; behavior resembles FCFS. Short jobs may wait longer.`
          : `Moderate quantum (${q}): Balanced overhead and fairness. Good general-purpose choice.`}</p>
    <br>
    <p><strong>Q6 — Which algorithm would you recommend?</strong><br>
    → <strong>Round Robin</strong> for interactive/time-sharing systems (guaranteed response).<br>
    → <strong>SRTF</strong> for batch/throughput-optimized systems (minimum WT and TAT).<br>
    → For this workload: <strong>${recommend}</strong>.</p>
  `;

  showAlert('Simulation complete! Scroll down to see the results.', 'success');
}

/* ══════════════════════════════════════════════════════════
   INITIALIZE — build the default 4-row input table on load
══════════════════════════════════════════════════════════ */
buildTable();