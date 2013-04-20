
package RFP;

import RFP.IO.RFPBinaryFile;
import RFP.IO.RFPBinaryFileI;
import RFP.UI.MainWindow;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FilePacker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try 
        {
            //simao testing place
            MainWindow mainform = new MainWindow();
         
          
           mainform.setEnabled(true);
           mainform.setVisible(true);

        } 
        catch (Exception ex) {
            Logger.getLogger(FilePacker.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }
}
