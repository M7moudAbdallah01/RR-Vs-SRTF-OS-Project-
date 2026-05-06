/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author 𝕐𝕠𝕦𝕤𝕤𝕚𝕗 𝔸𝕕𝕝𝕪
 */
import java.util.List;

public class InputValidator {

    public static boolean validateProcesses(List<Process> processes) {
        for (Process p : processes) {
            if (p.arrivalTime < 0 || p.burstTime <= 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateQuantum(int q) {
        return q > 0;
    }
}