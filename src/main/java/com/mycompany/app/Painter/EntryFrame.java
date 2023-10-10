package com.mycompany.app.Painter;

import com.mycompany.app.Parser.ImportPNML;
import com.mycompany.app.PetriNet.PetriNet;
import fr.lip6.move.pnml.framework.utils.exception.ImportException;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class EntryFrame extends JFrame implements ActionListener {

    private JButton upload = new JButton("upload");
    private File file;

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if(Objects.equals(action, "upload")) {

            JFileChooser fileChooser = new JFileChooser();

            // Show the file select box to the user
            int result = fileChooser.showOpenDialog(null);

            // Did the user select the "Open" button
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                String filename = file.getName();
                int dotIndex = filename.lastIndexOf(".");
                String extension = filename.substring(dotIndex + 1);

                ImportPNML importPNML = new ImportPNML(file);
                PetriNet petriNet = null;
                try {
                    if(extension.equals("txt")){
                        petriNet = importPNML.importHighLevel();
                    }else{
                        petriNet = importPNML.importPNML();
                    }
                } catch (InvalidIDException | VoidRepositoryException | ImportException | IOException ex) {
                    throw new RuntimeException(ex);
                }

                dispose();
                Frame frame = new com.mycompany.app.Painter.Frame(petriNet);
                System.out.println("File chosen: " + fileChooser.getSelectedFile());
            } else {
                file = new File("/Users/tonysken/IdeaProjects/myswingapp/src/main/java/com/mycompany/app/Parser/factorize2.pnml");
                System.out.println("No file chosen");
            }
        }
    }

    public EntryFrame(){
        setSize(300,300);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        upload.addActionListener(this);
        upload.setActionCommand("upload");
        upload.setPreferredSize(new Dimension(100, 100));
        add(upload, gbc);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
