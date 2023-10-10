package com.mycompany.app.PetriNet;

import com.mycompany.app.Painter.Algorithm;
import fr.lip6.move.pnml.ptnet.impl.PlaceImpl;

import java.awt.*;
import java.util.HashSet;

public class Place {
    protected String id;
    private long numberOfTokenInit;
    protected String label;

    protected float x;
    protected float y;
    protected int x_dim;
    protected int y_dim;
    protected String name;

    public Place() {

    }

    public Place(PlaceImpl place) {
        this.id = place.getId();
        this.label = place.getName().getText();
        this.numberOfTokenInit = place.getInitialMarking().getText();
        this.name = place.getName().getText();
        this.x = place.getNodegraphics().getPosition().getX();
        this.y = place.getNodegraphics().getPosition().getY();
        this.x_dim = place.getNodegraphics().getDimension().getX();
        this.y_dim = place.getNodegraphics().getDimension().getY();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public long getNumberOfTokenInit() {
        return numberOfTokenInit;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setNumberOfTokenInit(long numberOfTokenInit) {
        this.numberOfTokenInit = numberOfTokenInit;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void setX_dim(int x_dim) {
        this.x_dim = x_dim;
    }

    public int getX_dim() {
        return x_dim;
    }

    public void setY_dim(int y_dim) {
        this.y_dim = y_dim;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.drawOval((int)x, (int)y, x_dim, x_dim);
        graphics.setColor(Color.gray);
        graphics.fillOval((int)x, (int)y, x_dim, x_dim);
        graphics.setColor(Color.BLACK);
        graphics.drawString(label, (int)x, (int)y);
        drawPoints(graphics);
    }

    public void drawPoints(Graphics graphics){
        long mark = this.numberOfTokenInit;
        HashSet<int[]> pointPositions = Algorithm.calculatePoints((int)x, (int)y, x_dim, (int)mark);
        for(int[] position: pointPositions){
            graphics.setColor(Color.blue);
            graphics.drawOval(position[0], position[1], 5,5);
            graphics.fillOval(position[0], position[1], 5,5);
        }
    }

}
