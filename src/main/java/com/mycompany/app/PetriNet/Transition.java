package com.mycompany.app.PetriNet;

import com.mycompany.app.Painter.Algorithm;
import fr.lip6.move.pnml.ptnet.Arc;
import fr.lip6.move.pnml.ptnet.impl.ArcImpl;
import fr.lip6.move.pnml.ptnet.impl.TransitionImpl;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Transition {
    protected String id;
    private ArrayList<Edge> inEdges = new ArrayList<>();
    private ArrayList<Edge> outEdges = new ArrayList<>();
    protected String label;

    protected float x;
    protected float y;
    protected int x_dim;
    protected int y_dim;
    protected boolean isSelected = false;
    private ArrayList<Edge> satisfyingEdges = new ArrayList<>();

    public Transition() {
    }

    public Transition(TransitionImpl transition) {
        this.id = transition.getId();
        this.label = transition.getName().getText();
        this.x = transition.getNodegraphics().getPosition().getX();
        this.y = transition.getNodegraphics().getPosition().getY();
        this.x_dim = transition.getNodegraphics().getDimension().getX();
        this.y_dim = transition.getNodegraphics().getDimension().getY();
        List<Arc> arcs = transition.getInArcs();
        for(Arc arc: arcs) {
            Edge edge = new Edge((ArcImpl) arc);
            inEdges.add(edge);
        }
        arcs = transition.getOutArcs();
        for(Arc arc: arcs) {
            Edge edge = new Edge((ArcImpl) arc);
            outEdges.add(edge);
        }
    }

    public void setY_dim(int y_dim) {
        this.y_dim = y_dim;
    }

    public void setX_dim(int x_dim) {
        this.x_dim = x_dim;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public int getX_dim() {
        return x_dim;
    }

    public int getY_dim() {
        return y_dim;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void draw(Graphics graphics) {
        if(isSelected){
            graphics.setColor(Color.RED);
            isSelected = false;
        }else {
            graphics.setColor(Color.BLACK);
        }
        graphics.drawRect((int)x, (int)y, x_dim, y_dim);
        drawColor(graphics);
        graphics.setColor(Color.black);
        graphics.drawString(label, (int)x, (int)y);
        drawPoints(graphics);
    }

    public void drawPoints(Graphics graphics){
        int mark = 0;
        for(Edge edge: satisfyingEdges){
            mark += edge.getCondition();
        }
        HashSet<int[]> pointPositions = Algorithm.calculatePointsSquare((int)x, (int)y, x_dim, y_dim, mark);
        for(int[] position: pointPositions){
            graphics.setColor(Color.blue);
            graphics.drawOval(position[0], position[1], 5,5);
            graphics.fillOval(position[0], position[1], 5,5);
        }
    }

    public void drawColor(Graphics graphics){
        graphics.setColor(Color.GRAY);
        graphics.fillRect((int)x, (int)y, x_dim, y_dim);
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<Edge> getInEdges() {
        return inEdges;
    }

    public ArrayList<Edge> getOutEdges() {
        return outEdges;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setSatisfyingEdges(ArrayList<Edge> satisfyingEdges) {
        this.satisfyingEdges = satisfyingEdges;
    }

    public ArrayList<Edge> getSatisfyingEdges() {
        return satisfyingEdges;
    }
}
