
package RFP.UI;

import RFP.IO.RFPBinaryFile;
import RFP.IO.RFPDirectoryHeader;
import RFP.IO.RFPException;
import RFP.IO.RFPFileHeader;
import java.awt.Dialog;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.filechooser.FileFilter;

/**
 * Form principal do programa que permite gerir o conteúdo do ficheiro RFP.
 *
 */
public class MainWindow extends javax.swing.JFrame 
{
    private RFPBinaryFile rfpFile = null;
    private int currDirectoryId = RFPBinaryFile.RFP_DIRECTORY_ROOT;
    private String nativePath = "";
    private JButton upDir = null;
    private JButton rfpNew = null;
    private JButton extractTo = null;
    private JButton fileDel = null;
    private JButton deldir = null;
    private JButton newdir = null;
    
    /**
     * Cria uma nova form mainWindow.
     * Esta form é a form principal do programa.
     */
    public MainWindow() 
    {
        initComponents();
        //adiciona um botao
        rfpNew = new JButton(new ImageIcon("rfp_new.png"));
        rfpNew.addActionListener(new java.awt.event.ActionListener() {
          
            /**
             * Accionado quando se clica no botao para criar um novo ficheiro
             * RFP.
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrfpnewActionPerformed(evt);
            }
            
            /**
             * Cria um novo ficheiro RFP.
             * Pergunta ao utilizador o caminho onde será guardado o ficheiro.
             * Fecha o ficheiro RFP actual caso exista e faz um reset ao
             * conteudo da tabela, mostrando de seguida o conteudo do novo
             * ficheiro RFP.
             * 
             * É lançado uma excepção caso seja impossivel criar o ficheiro na
             * directoria de destino.
             */
            private void jrfpnewActionPerformed(ActionEvent evt) {
                try 
                {
                    final JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showSaveDialog(MainWindow.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) 
                    {
                        File dir = fc.getCurrentDirectory();
                        File file = fc.getSelectedFile();

                        if(rfpFile != null && !rfpFile.isClosed())
                                rfpFile.closeRFP();
                        resetTable();
                        resetVariables();

                        rfpFile = new RFPBinaryFile(file.getName(), 
                                dir.getPath());
                        rfpFile.openRFP();
                        rfpFile.generateEmptyFile();
                        rfpFile.readContents();
                        nativePath = dir.getPath();
                        showRFPContents();
                    }
                } 
                catch (Exception ex) 
                {
                    JOptionPane.showMessageDialog(null, 
                            "Impossivel criar ficheiro rfp.", 
                            "Novo Ficheiro RFP", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //adiciona o botao a toolbar
        this.jToolBar1.add(rfpNew);
        
        extractTo = new JButton(new ImageIcon("rfp_extract.png"));
        extractTo.addActionListener(new java.awt.event.ActionListener() {
            /**
             *  Accionado quando se clica no botao para extrair ficheiros ou 
             *  pastas.
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extractToActionPerformed(evt);
            }

            /**
             * Permite extrair ficheiros ou pastas do ficheiro RFP.
             * Se não ouver linhas selecionadas, será extraido todo o conteudo
             * da pasta actual.
             * Se for selecionado um ficheiro, será extraido o ficheiro
             * selecionado.
             * Se for selecionado uma pasta, será extraido a pasta selecionada.
             * 
             * É lançado uma excepção caso a extracção falhe.
             */
            private void extractToActionPerformed(ActionEvent evt) {
                try 
                {
                    //verificar a seleccão
                    int row = jTable1.getSelectedRow();
                    //se nada foi selecionado, extrair todo o seu conteudo para
                    //a pasta actual.
                    if (row == -1)
                    {
                         final JFileChooser fc = new JFileChooser();
                         fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                         int returnVal = fc.showOpenDialog(MainWindow.this);
                         if (returnVal == JFileChooser.APPROVE_OPTION) 
                         { 
                             File dir = fc.getCurrentDirectory();
                             File dest = fc.getSelectedFile();
                             rfpFile.extractAll(currDirectoryId, 
                                     dest.getPath());
                         }
                    }
                    else
                    {
                        //obter o objecto que esta na linha selecionada.
                         Object obj = jTable1.getModel().getValueAt(row, 0);
                         //verificar se o objecto é um ficheiro
                         if(obj instanceof RFPFileHeader)
                         {
                             final JFileChooser fc = new JFileChooser();
                             fc.setFileSelectionMode(
                                     JFileChooser.DIRECTORIES_ONLY);
                             int returnVal = fc.showOpenDialog(MainWindow.this);
                             if (returnVal == JFileChooser.APPROVE_OPTION) 
                             {
                                 File dir = fc.getCurrentDirectory();
                                 File dest = fc.getSelectedFile();
                                 if( (int)(((RFPFileHeader)obj).getFlags() & 
                                         RFPBinaryFile.RFP_COMPRESSED) == 
                                         RFPBinaryFile.RFP_COMPRESSED)
                                 {
                                     rfpFile.decompressFile(
                                           ((RFPFileHeader)obj).getFilenameId(),
                                             dest.getPath());
                                 }
                                 else
                                 {
                                     rfpFile.extractFile(
                                           ((RFPFileHeader)obj).getFilenameId(),
                                        dest.getPath());
                                 }
                             }
                         }
                         //verificar se o objecto é uma directoria.
                         else if(obj instanceof RFPDirectoryHeader)
                         {
                              final JFileChooser fc = new JFileChooser();
                              fc.setFileSelectionMode(
                                      JFileChooser.DIRECTORIES_ONLY);
                              int returnVal = 
                                      fc.showOpenDialog(MainWindow.this);
                              if (returnVal == JFileChooser.APPROVE_OPTION) 
                              {
                                  File dir = fc.getCurrentDirectory();
                                  File dest = fc.getSelectedFile();
                                  rfpFile.extractAll(
                                          ((RFPDirectoryHeader)obj).getId(),
                                          dest.getPath());
                              }
                         }
                   }
                } catch (Exception ex) {
                   JOptionPane.showMessageDialog(null, 
                            "Impossivel extrair ficheiro.", 
                            "Extração de Ficheiro", 
                            JOptionPane.ERROR_MESSAGE);
                } 
            }
        });
        
        //adiciona o botao a toolbar.
        this.jToolBar1.add(extractTo);
        
        fileDel = new JButton(new ImageIcon("file_del.png"));
        fileDel.addActionListener(new java.awt.event.ActionListener() {
             /**
             *  Accionado quando se clica no botao para apagar um ficheiro.
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               fileDelActionPerformed(evt);
             }
            
            /**
             * Permite apagar o ficheiro selecionado do ficheiro RFP.
             * Verifica se a linha selecionada é um ficheiro.
             * Caso seja um ficheiro, apaga-o e actualiza a tabela de conteudos
             * do ficheiro RFP.
             * 
             * É lançado uma excepção caso seja impossivel apagar o ficheiro.
             */
            private void  fileDelActionPerformed(java.awt.event.ActionEvent evt)
            {
                try 
                {
                    //verificar a seleccão
                    int row = jTable1.getSelectedRow();
                    Object obj = jTable1.getModel().getValueAt(row, 0);
                    if(obj instanceof RFPFileHeader)
                    {
                         rfpFile.removeFile(
                                 ((RFPFileHeader)obj).getFilenameId());
                         showRFPContents();

                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, 
                            "Impossivel exclusão do ficheiro.", 
                            "Exclusão de Ficheiro", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                 } 
                catch (Exception ex) {
                JOptionPane.showMessageDialog(null, 
                            "Impossivel exclusão do ficheiro.", 
                            "Exclusão de Ficheiro", 
                            JOptionPane.ERROR_MESSAGE);
                }     
            } 
        });
        
        //adiciona o botao a toolbar
        this.jToolBar1.add(fileDel);
        
        upDir = new JButton(new ImageIcon("folder_up.png"));
        upDir.setEnabled(false);
        upDir.addActionListener(new java.awt.event.ActionListener() {
            
             /**
             *  Accionado quando se clica no botao para voltar a pasta anterior. 
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               upDirActionPerformed(evt);
            }

            /**
             * Mostra o conteudo da pasta anterior a actual.
             * 
             * O botão é desactivado caso a pasta anterior seja a raiz.
             * É lançado uma excepção caso seja impossivel voltar a pasta
             * anterior.
             */
            private void upDirActionPerformed(ActionEvent evt) {
                try{
                    RFPDirectoryHeader dir = 
                            rfpFile.getDirectoryId(currDirectoryId);
                    currDirectoryId = dir.getParentId();
                    rfpFile.setCurrDirectory(currDirectoryId);
                    if (currDirectoryId == 0) 
                    {
                        upDir.setEnabled(false);
                    }
                    showRFPContents();
                }
                catch(Exception e){}
            }
        });
               
        //adiciona o botao a toolbar.       
        this.jToolBar1.add(upDir);
        
        newdir = new JButton(new ImageIcon("folder_new.png"));
        newdir.addActionListener(new java.awt.event.ActionListener() {
             /**
             *  Accionado quando se clica no botao para criar uma  pasta.
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               newdirActionPerformed(evt);
            }

            /**
             * Cria uma nova pasta na directoria actual.
             * Se não for introduzido um nome, será mostrado uma mensagem de
             * erro.
             * 
             * É lançado uma excepção caso não seja possivel criar a directoria.
             */
            private void newdirActionPerformed(ActionEvent evt) {
                try 
                {
                    String input = 
                            JOptionPane.showInputDialog("Nome do directorio");
                    if(input.length() > 0)
                    {

                        rfpFile.makeDirectory(currDirectoryId, input);
                        showRFPContents();
                    }   
                    else
                    {
                        JOptionPane.showMessageDialog(null, 
                                "Nome de directoria invalida",
                                "Adicionar Directoria",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } 
                catch (Exception ex)
                { 
                    JOptionPane.showMessageDialog(null, 
                            "Impossível Criar Directoria.", 
                            "Criação de Directoria", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //adiciona o botao a toolbar.
        this.jToolBar1.add(newdir);
        
        deldir = new JButton(new ImageIcon("folder_remove.png"));
        deldir.addActionListener(new java.awt.event.ActionListener() {
             /**
             *  Accionado quando se clica no botao para apagar uma pasta. 
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               deldirActionPerformed(evt);
            }

            /**
             * Permite apagar u pasta selecionada.
             * Verifica se a linha selecionada é uma directoria.
             * Caso seja uma directoria, apaga-a.
             * 
             * É lançado uma excepção caso não seja possivel apagar a pasta
             * selecionada.
             */
            private void deldirActionPerformed(ActionEvent evt) {
                try 
                {
                    //verificar a seleccão
                   int row = jTable1.getSelectedRow();
                   Object obj = jTable1.getModel().getValueAt(row, 0);
                   if(obj instanceof RFPDirectoryHeader)
                   {
                        rfpFile.removeDirectory(
                                ((RFPDirectoryHeader)obj).getId());
                        showRFPContents();

                   }
                   else
                   {
                       JOptionPane.showMessageDialog(null, 
                                    "Impossível Excluir Directoria.", 
                                    "Exclusão de Directoria", 
                                    JOptionPane.ERROR_MESSAGE);
                       
                   }
                } 
                catch (Exception ex) {
                 JOptionPane.showMessageDialog(null, 
                                    "Impossível Excluir Directoria.", 
                                    "Exclusão de Directoria", 
                                    JOptionPane.ERROR_MESSAGE);
                 } 
            }
        });
        
        //adiciona o botao a toolbar.
        this.jToolBar1.add(deldir);
    }
    
    /**
     * Faz um reset às variáveis de instância.
     */
    public void resetVariables()
    {
        rfpFile = null;
        currDirectoryId = RFPBinaryFile.RFP_DIRECTORY_ROOT;
        nativePath = "";
    }
    
    /**
     * Faz um reset ao conteudo da tabela, deixando a tabela vazia.
     */
    public void resetTable()
    {
        javax.swing.table.DefaultTableModel model =  
                new javax.swing.table.DefaultTableModel();
        model.setColumnCount(3);
        model.setColumnIdentifiers(new String [] {
                "Nome", "Tamanho", "CRC" });
        jTable1.setModel(model);
    }
    
    /**
     * Mostra o conteudo do ficheiro RFP para a directoria actual.
     * Faz uma pesquisa pelas pastas  e ficheiros que estão dentro da directoria
     * actual.
     */
    public void showRFPContents()
    {
        javax.swing.table.DefaultTableModel model =  
                new javax.swing.table.DefaultTableModel();
        
        //contar a quantidade de linhas
        int rows = rfpFile.countDirFilesAtParent(currDirectoryId);
        model.setNumRows(rows);
        model.setColumnCount(3);
       
        model.setColumnIdentifiers(new String [] {
                 "Nome", "Tamanho", "CRC" });
       

        int rowCount = 0;
        for(RFPDirectoryHeader dir : rfpFile.getDirectoryList() )
        {
            if(dir.getParentId() == currDirectoryId)
            {
               // model.setValueAt("", rowCount, 0);
                model.setValueAt(dir, rowCount, 0);
                model.setValueAt("", rowCount, 1);
                model.setValueAt("", rowCount, 2);
                rowCount++;
            }
        }
        
        for(RFPFileHeader file : rfpFile.getFileList() )
        {
            if(file.getDirectoryId() == currDirectoryId)
            {
                model.setValueAt(file, rowCount, 0);
                model.setValueAt(file.getSize(), rowCount, 1);
                
                //converter o CRC para uppercase e hexadecimal
                model.setValueAt(Integer.toHexString(
                        file.getCrc32()).toUpperCase(), rowCount, 2);
                rowCount++;
            }
        }
        this.jTable1.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 =  new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        jToolBar1 = new javax.swing.JToolBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Create");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ramos FilePacker");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Tamanho", "CRC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFillsViewportHeight(true);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MainWindow.this.mouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jMenu1.setText("File");

        jMenuItem2.setText("Abrir Ficheiro RFP");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem14.setText("Novo Ficheiro RFP");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem14);

        jMenuItem3.setText("Fechar Ficheiro RFP");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenu1.add(jSeparator1);

        jMenuItem1.setText("Sair");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Opções");

        jMenuItem4.setText("Adicionar Ficheiro");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Adicionar e Comprimir Ficheiro");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Extrair Ficheiro");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Extrair Todos");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setText("Remover Ficheiro");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem12.setText("Visualizar Ficheiro");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);
        jMenu2.add(jSeparator2);

        jMenuItem9.setText("Remover Directoria");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setText("Adicionar Directoria");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem11.setText("Extrair Directoria");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);
        jMenu2.add(jSeparator3);

        jMenuItem13.setText("Testar Integridade");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem13);
        jMenu2.add(jSeparator4);

        jMenuItem16.setText("Gerar Ficheiro RFP De Teste");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem16);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Desenvolvido por Daniel Ramos e Simão Ramos");
        jMenu3.setEnabled(false);
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Accionado quando se faz duplo clique com o rato na tabela.
     * Activa o botao que permite retornar a pasta anterior e se o objecto 
     * selecionado for uma pasta, muda os conteudos da tabela para o conteudo
     * da nova pasta.
     * 
     * O duplo clique só é valido caso o objecto selecionado seja uma
     * directoria.
     * 
     * É lançado uma excepção caso seja feito um duplo clique sem selecção.
     * @param evt 
     */
    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
      
        if(evt.getClickCount() == 2)
        {
            //clique duplo
            try {
                  
                  int row = jTable1.getSelectedRow();
                  Object obj = jTable1.getModel().getValueAt(row, 0);
                  if(obj instanceof RFPDirectoryHeader)
                  {

                       upDir.setEnabled(true);
                       this.resetTable();
                       this.currDirectoryId = ((RFPDirectoryHeader)obj).getId();
                       rfpFile.setCurrDirectory(currDirectoryId);
                       showRFPContents();
                  }

           } 
            catch (Exception ex) {
               
           } 
        }
    }//GEN-LAST:event_mouseClicked


     /**
     * Accionado quando se faz clique no botao para abrir um ficheiro RFP
     * Permite selecionar um ficheiro rfp escolhido pelo utilizador.
     * Fecha o ficheiro actual caso este exista e faz um reset ao conteu
     * do actual da tabela, mostrando de seguida os novos conteudos do ficheiro
     * escolhido.
     * 
     * É lançado uma excepção caso seja feito um duplo clique sem selecção.
     * @param evt 
     */
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
 
        try
        {
            final JFileChooser fc = new JFileChooser();
            RFPFileFilter filter = new RFPFileFilter();
            fc.addChoosableFileFilter(filter);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                File dir = fc.getCurrentDirectory();
                //fechar o ficheiro actual
                if(rfpFile != null && !rfpFile.isClosed())
                     rfpFile.closeRFP();

                resetTable();
                resetVariables();
                rfpFile = new RFPBinaryFile(file.getName(), dir.getPath());
                rfpFile.openRFP();
                rfpFile.readContents();
                nativePath = dir.getPath();
                showRFPContents();
            }
        }
        catch(Exception e)
        {
             JOptionPane.showMessageDialog(null, 
                            "Impossivel abrir o ficheiro rfp.", 
                            "Abrir Ficheiro RFP", 
                            JOptionPane.ERROR_MESSAGE);
            
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * Accionado quando se faz clique no botao para fechar um ficheiro RFP
     * Fecha o ficheiro RFP actual, limpando o conteudo da tabela.
     * 
     * É lançado uma excepção caso seja feito um duplo clique sem selecção.
     * @param evt 
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try 
        {
            rfpFile.closeRFP();
            resetVariables();
            resetTable();

        } 
        catch (IOException ex) 
        {
             JOptionPane.showMessageDialog(null, 
                            "Impossivel fechar o ficheiro rfp.", 
                            "Fechar Ficheiro RFP", 
                            JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * Accionado quando se faz clique no botao para adicionar um ficheiro ao
     * ficheiro RFP.
     * Permite adicionar um ficheiro escolhido pelo utilizador..
     * 
     * É lançado uma excepção caso seja impossivel adicionar o ficheiro.
     * @param evt 
     */
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try
        {   
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                File dir = fc.getCurrentDirectory();
                rfpFile.addFile(file.getName(),dir.getPath(), 
                        RFPBinaryFile.RFP_NO_FLAG);
                showRFPContents();
            }
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog(null, 
                            "Impossível adicionar ficheiro.", 
                            "Adicionar Ficheiro", 
                            JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * Accionado quando se faz clique no botao para extrair uma directoria do
     * ficheiro RFP.
     * 
     * Permite extrair uma directoria do ficheiro RFP na pasta de destino
     * escolhida pelo utilizador.
     * 
     * Se o objecto selecionado for uma directoria, irá então ser extraido o
     * conteúdo dessa mesma directoria.

     * É lançado uma excepção caso seja impossivel extrair os conteúdos da
     * directoria selecionada.
     * @param evt 
     */
    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try 
        {
            //verificar a seleccão
           int row = jTable1.getSelectedRow();
           Object obj = jTable1.getModel().getValueAt(row, 0);
           if(obj instanceof RFPDirectoryHeader)
           {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) 
                {
                    File dir = fc.getCurrentDirectory();
                    File dest = fc.getSelectedFile();
                    rfpFile.extractAll(((RFPDirectoryHeader)obj).getId(), 
                            dest.getPath());
                }
           }
           else
           {
               JOptionPane.showMessageDialog(null, 
                            "Impossível extrair directoria.", 
                            "Extrair Directoria", 
                            JOptionPane.ERROR_MESSAGE);
           }
           
           
         } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                            "Impossível extrair directoria.", 
                            "Extrair Directoria", 
                            JOptionPane.ERROR_MESSAGE);
         } 
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    /**
     * Accionado quando se faz clique no botao para criar um ficheiro RFP.
     * 
     * Cria um novo ficheiro RFP.
     * Pergunta ao utilizador o caminho onde será guardado o ficheiro.
     * Fecha o ficheiro RFP actual caso exista e faz um reset ao
     * conteudo da tabela, mostrando de seguida o conteudo do novo
     * ficheiro RFP.
     * 
     * É lançado uma excepção caso seja impossivel criar o ficheiro na
     * directoria de destino.
     *
     * @param evt 
     */
    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        try 
        {
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File dir = fc.getCurrentDirectory();
                File file = fc.getSelectedFile();
                
                if(rfpFile != null && !rfpFile.isClosed())
                        rfpFile.closeRFP();
                this.resetTable();
                this.resetVariables();
               
                rfpFile = new RFPBinaryFile(file.getName(), dir.getPath());
                rfpFile.openRFP();
                rfpFile.generateEmptyFile();
                rfpFile.readContents();
                nativePath = dir.getPath();
                this.showRFPContents();
            }
        } 
        catch (Exception ex) 
        {
             JOptionPane.showMessageDialog(null, 
                            "Impossivel criar ficheiro rfp.", 
                            "Novo Ficheiro RFP", 
                            JOptionPane.ERROR_MESSAGE);
         }
        
    }//GEN-LAST:event_jMenuItem14ActionPerformed
    
    /**
     * Accionado quando se faz clique no botao para gerar um ficheiro RFP
     * de testes vazio.
     * 
     * É lançado uma excepção caso seja impossivel gerar o ficheiro na 
     * directoria actual.
     *
     * @param evt 
    */
    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        try 
        {
            RFPBinaryFile rfp = new RFPBinaryFile("teste.rfp", "");
            rfp.generateEmptyFile();
        } 
        catch (Exception ex) {
             JOptionPane.showMessageDialog(null, 
                            "Impossivel gerar ficheiro rfp.", 
                            "Gerar Ficheiro RFP", 
                            JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_jMenuItem16ActionPerformed

        
    /**
     * Accionado quando se faz clique no botao para adicionar um ficheiro
     * comprimido ao ficheiro RFP.
     * 
     * É lançado uma excepção caso seja impossivel adicionar o ficheiro.
     *
     * @param evt 
    */
    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        try {
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                File dir = fc.getCurrentDirectory();
                rfpFile.addCompressedFile(file.getName(),dir.getPath(), 
                        RFPBinaryFile.RFP_NO_FLAG);
                showRFPContents();
            }
         } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                            "Impossível adicionar e comprimir ficheiro.", 
                            "Adicionar e Comprimir Ficheiro", 
                            JOptionPane.ERROR_MESSAGE);;
         } 
    }//GEN-LAST:event_jMenuItem5ActionPerformed

        
    /**
     * Accionado quando se faz clique no botao para extrair um ficheiro.
     * 
     * Permite extrair o conteudo de um ficheiro selecionado pelo utilizador.
     * A extracção só será possivel caso o objecto selecionado seja um ficheiro.
     * 
     * É lançado uma excepção caso a extracção falhe.
     *
     * @param evt 
    */
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try 
        {
            //verificar a seleccão
           int row = jTable1.getSelectedRow();
           Object obj = jTable1.getModel().getValueAt(row, 0);
           if(obj instanceof RFPFileHeader)
           {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) 
                {
                    File dir = fc.getCurrentDirectory();
                    File dest = fc.getSelectedFile();
                    
                    if( (int)(((RFPFileHeader)obj).getFlags() & 
                            RFPBinaryFile.RFP_COMPRESSED) == 
                            RFPBinaryFile.RFP_COMPRESSED)
                    {
                        rfpFile.decompressFile(
                                ((RFPFileHeader)obj).getFilenameId(), 
                                dest.getPath());
                        
                    }
                    else
                    {
                        rfpFile.extractFile(
                                ((RFPFileHeader)obj).getFilenameId(),
                            dest.getPath());
                    }
                }
           }
           else
           {
               JOptionPane.showMessageDialog(null, 
                            "Impossivel extrair ficheiro.", 
                            "Extração de Ficheiro", 
                            JOptionPane.ERROR_MESSAGE);  
           }
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                            "Impossivel extrair ficheiro.", 
                            "Extração de Ficheiro", 
                            JOptionPane.ERROR_MESSAGE);
        } 
        
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    /**
     * Accionado quando se faz clique no botao para extrair todos os ficheiros.
     * 
     * Permite extrair todo o conteudo do ficheiro rfp para a directoria
     * selecionada pelo utilizador.
     * 
     * É lançado uma excepção caso a extracção falhe.
     *
     * @param evt 
    */
    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        
        try 
        {
            //verificar a seleccão
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            { 
                File dir = fc.getCurrentDirectory();
                File dest = fc.getSelectedFile();
                rfpFile.extractAll(0, dest.getPath());
            }
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                            "Impossivel extrair todos os ficheiros.", 
                            "Extrair Todos Os Ficheiros", 
                            JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    /**
     * Accionado quando se faz clique no botao para eliminar um ficheiro.
     * 
     * Permite eliminar um ficheiro do ficheiro RFP.
     * A eliminação só será possivel caso o objecto selecionado seja um 
     * ficheiro.
     * 
     * É lançado uma excepção caso a eliminação falhe.
     *
     * @param evt 
    */
    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        try 
        {
             //verificar a seleccão
            int row = jTable1.getSelectedRow();
            Object obj = jTable1.getModel().getValueAt(row, 0);
            if(obj instanceof RFPFileHeader)
            {
                 rfpFile.removeFile(((RFPFileHeader)obj).getFilenameId());
                 showRFPContents();
            }
            else
            {
                JOptionPane.showMessageDialog(null, 
                             "Impossivel exclusão do ficheiro.", 
                             "Exclusão de Ficheiro", 
                             JOptionPane.ERROR_MESSAGE);
                
            }
        } 
        catch (Exception ex) {
             JOptionPane.showMessageDialog(null, 
                             "Impossivel exclusão do ficheiro.", 
                             "Exclusão de Ficheiro", 
                             JOptionPane.ERROR_MESSAGE);
        } 
        
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    /**
     * Accionado quando se faz clique no botao para remover uma directoria.
     * 
     * Permite remover uma directoria e todo o seu conteudo.
     * A remoção só será possivel caso o objecto selecionado seja uma 
     * directoria.
     * 
     * É lançado uma excepção caso a eliminação falhe.
     *
     * @param evt 
    */
    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        try 
        {
            //verificar a seleccão
            int row = jTable1.getSelectedRow();
            Object obj = jTable1.getModel().getValueAt(row, 0);
            if(obj instanceof RFPDirectoryHeader)
            {
                 rfpFile.removeDirectory(((RFPDirectoryHeader)obj).getId());
                 showRFPContents();

            }
            else
            {
                JOptionPane.showMessageDialog(null, 
                            "Impossível Excluir Directoria.", 
                            "Exclusão de Directoria", 
                            JOptionPane.ERROR_MESSAGE);
            }
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                            "Impossível Excluir Directoria.", 
                            "Exclusão de Directoria", 
                            JOptionPane.ERROR_MESSAGE);
         } 
        
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    /**
     * Accionado quando se faz clique no botao para criar uma directoria.
     * 
     * Cria uma nova pasta na directoria actual.
     * Se não for introduzido um nome, será mostrado uma mensagem de
     * erro.
     * 
     * É lançado uma excepção caso não seja possivel criar a directoria.
     *
     * @param evt 
    */
    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        try {
            String input = JOptionPane.showInputDialog("Nome do directorio");
            if(input.length() > 0)
            {

                rfpFile.makeDirectory(this.currDirectoryId, input);
                this.showRFPContents();
            }   
            else
            {
                JOptionPane.showMessageDialog(null, 
                        "Nome de directoria invalida", 
                        "Adicionar Directoria", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } 
        catch (Exception ex) 
        {
                JOptionPane.showMessageDialog(null, 
                            "Impossível Criar Directoria.", 
                            "Criação de Directoria", 
                            JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Accionado quando se faz clique no botao para testar a integridade do
     * ficheiro RFP.
     * 
     * 
     * É lançado uma excepção caso a integridade falhe.
     *
     * @param evt 
    */
    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        try {
            
            rfpFile.testIntegrity();
        } 
        catch (Exception ex) 
        {
                JOptionPane.showMessageDialog(null, 
                            "Corrupção detectada.", 
                            "Falha no Teste de Integridade", 
                            JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    /**
     * Accionado quando se faz clique no botao para visualizar um ficheiro
     * 
     * É lançado uma excepção caso a visualizacao falhe.
     *
     * @param evt 
    */
    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        try
        {
            int row = jTable1.getSelectedRow();
            Object obj = jTable1.getModel().getValueAt(row, 0);
            if(obj instanceof RFPFileHeader)
            {
                 rfpFile.showFileContent(((RFPFileHeader)obj).getFilenameId());
            }
            else
            {
                JOptionPane.showMessageDialog(null, 
                            "Impossivel visualizar o ficheiro", 
                            "Falha ao visualizar ficheiro", 
                            JOptionPane.ERROR_MESSAGE);  
            }
        }
        catch(Exception ex) 
        {
                JOptionPane.showMessageDialog(null, 
                            "Impossivel visualizar o ficheiro", 
                            "Falha ao visualizar ficheiro", 
                            JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed
    
    /**
     * Accionado quando se faz clique no botao para sair do programa
     *
     * @param evt 
    */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
       
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
