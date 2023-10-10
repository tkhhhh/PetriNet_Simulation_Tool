package com.mycompany.app.Painter;

import com.mycompany.app.Painter.ColoredController.ColoredController;
import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredPetriNet;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPetriNet;
import com.mycompany.app.PetriNet.PetriNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class Frame extends JFrame implements ActionListener {
    private JPanel panel = new JPanel(new BorderLayout());
    private Drawer drawer;
    private JPanel controlPanel = new JPanel();
    private JButton button = new JButton("next");
    private PetriNet petriNet;
    private ColoredPetriNet coloredPetriNet;
    private HighLevelPetriNet highLevelPetriNet;
    private Controller controller;
    private ColoredController coloredController;

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if(Objects.equals(action, "next")){
            if(petriNet != null){
                controller.executeTransition();
                petriNet = controller.getPetriNet();
                panel.remove(drawer);
                drawer = new Drawer(petriNet);
            }
            if(coloredPetriNet != null){
                coloredController.executeTransition();
                coloredPetriNet = coloredController.getColoredPetriNet();
                panel.remove(drawer);
                drawer = new Drawer(coloredPetriNet);
            }
            panel.add(drawer, BorderLayout.CENTER);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    public Frame(PetriNet petriNet){
        if(petriNet.getClass() == PetriNet.class){
            this.petriNet = petriNet;
            this.controller = new Controller(petriNet);
            this.drawer = new Drawer(this.petriNet);
        } else if (petriNet.getClass() == ColoredPetriNet.class) {
            this.coloredPetriNet = (ColoredPetriNet) petriNet;
            this.coloredController = new ColoredController(coloredPetriNet);
            this.drawer = new Drawer(coloredPetriNet);
        } else {
            this.highLevelPetriNet = (HighLevelPetriNet) petriNet;
            this.drawer = new Drawer(highLevelPetriNet);
        }
        setSize(600,1000);

        button.addActionListener(this);
        button.setActionCommand("next");
        controlPanel.add(button);



        //JScrollPane jScrollPane = new JScrollPane();
        //jScrollPane.setViewportView(drawer);
        //add(jScrollPane, BorderLayout.CENTER);
        panel.add(drawer, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        //add(new JScrollPane(drawer), BorderLayout.CENTER);
        //add(controlPanel, BorderLayout.SOUTH);
        add(panel);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
                EntryFrame entryFrame = new EntryFrame();
            }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}