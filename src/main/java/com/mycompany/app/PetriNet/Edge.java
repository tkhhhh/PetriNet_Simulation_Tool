package com.mycompany.app.PetriNet;

import fr.lip6.move.pnml.ptnet.Position;
import fr.lip6.move.pnml.ptnet.impl.ArcImpl;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Edge {
    protected String id;
    protected String source;
    protected String target;
    protected String label;
    private long condition;

    protected ArrayList<float[]> route = new ArrayList<>();

    public Edge() {
    }

    public long getCondition() {
        return condition;
    }

    public void setRoute(ArrayList<float[]> route) {
        this.route = route;
    }

    public Edge(ArcImpl arc) {
        this.id = arc.getId();
        this.source = arc.getSource().getId();
        this.target = arc.getTarget().getId();
        this.label = "edge";
        this.condition = arc.getInscription().getText();
        for(Position position : arc.getArcgraphics().getPositions()){
            int x = position.getX();
            int y = position.getY();
            route.add(new float[]{x,y});
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<float[]> getRoute() {
        return route;
    }

    public void draw(Graphics graphics) {
        int index = 1;
        while(index < route.size()) {
            if(index == route.size() / 2){
                int x = (int)(route.get(index - 1)[0] + route.get(index)[0]) / 2;
                int y = (int)(route.get(index - 1)[1] + route.get(index)[1]) / 2;
                graphics.setColor(Color.black);
                graphics.drawString(covertCondition(), x, y);
            }
            graphics.setColor(Color.black);
            graphics.drawLine((int)route.get(index - 1)[0], (int)route.get(index - 1)[1], (int)route.get(index)[0], (int)route.get(index)[1]);
            index++;
        }
        //graphics.drawLine((int)route.get(index - 1)[0], (int)route.get(index - 1)[1], (int)route.get(index)[0], (int)route.get(index)[1]);
        graphics.setColor(Color.red);
        graphics.drawOval((int)route.get(index - 1)[0] - 5, (int)route.get(index - 1)[1] - 5, 10,10);
        graphics.setColor(Color.red);
        graphics.fillOval((int)route.get(index - 1)[0] - 5, (int)route.get(index - 1)[1] - 5, 10,10);
        graphics.setColor(Color.gray);
    }

    public String covertCondition(){
        return String.valueOf(condition);
    }

}
