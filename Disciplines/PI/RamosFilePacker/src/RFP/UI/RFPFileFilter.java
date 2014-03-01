
package RFP.UI;

import RFP.Util.Utilitaries;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.*;

/**
 * Class que permite adicionar um filtro a janela de selecção de ficheiros.
 */
public class RFPFileFilter extends FileFilter
{
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension = Utilitaries.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utilitaries.rfp)) {
                    return true;
            } else {
                return false;
            }
        }
 
        return false;
    }
 
    @Override
    public String getDescription() {
        return "*.RFP";
    }
}
