package com.mycompany.app.Parser;

import com.mycompany.app.Parser.HighLevel.AnalysisOfSegment;
import com.mycompany.app.PetriNet.ColoredPetriNet.*;
import com.mycompany.app.PetriNet.Edge;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelEdge;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPetriNet;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPlace;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelTransition;
import com.mycompany.app.PetriNet.PetriNet;
import com.mycompany.app.PetriNet.Place;
import com.mycompany.app.PetriNet.Transition;
import fr.lip6.move.pnml.framework.hlapi.HLAPIRootClass;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import fr.lip6.move.pnml.framework.utils.PNMLUtils;
import fr.lip6.move.pnml.framework.utils.exception.*;
import fr.lip6.move.pnml.hlpn.hlcorestructure.Declaration;
import fr.lip6.move.pnml.hlpn.terms.TermsDeclaration;
import fr.lip6.move.pnml.hlpn.terms.impl.VariableDeclImpl;
import fr.lip6.move.pnml.ptnet.Page;
import fr.lip6.move.pnml.ptnet.PnObject;
import fr.lip6.move.pnml.ptnet.impl.ArcImpl;
import fr.lip6.move.pnml.ptnet.impl.PageImpl;
import fr.lip6.move.pnml.ptnet.impl.PlaceImpl;
import fr.lip6.move.pnml.ptnet.impl.TransitionImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImportPNML {
    public PetriNet petriNet;
    public ColoredPetriNet coloredPetriNet;
    public HighLevelPetriNet highLevelPetriNet;
    public File file;

    public ImportPNML(File file){
        this.file = file;
    }

    public PetriNet importHighLevel() throws IOException {
        AnalysisOfSegment analysisOfSegment = new AnalysisOfSegment(file);
        highLevelPetriNet = analysisOfSegment.getHighLevelPetriNet();
        return highLevelPetriNet;
    }

    public static void main(String[] args) throws IOException {
        ImportPNML importPNML = new ImportPNML(new File("/Users/tonysken/high.txt"));
        PetriNet petriNet = importPNML.importHighLevel();
        petriNet.getPlaces();
    }

    public PetriNet importPNML() throws InvalidIDException, VoidRepositoryException, ImportException {
        //ModelRepository.getInstance().createDocumentWorkspace("test");

        HLAPIRootClass rc = PNMLUtils.importPnmlDocument(file, false);;

        if (PNMLUtils.isPTNetDocument(rc)) {
            // process the Place/Transition Net document, get the right type first
            fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI ptDoc =
                    (fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI)rc;
            // only process one petri net for testing
            petriNet = new PetriNet();
            petriNet.setId(ptDoc.getNets().get(0).getId());
            List<Page> pages = ptDoc.getNets().get(0).getPages();
            for(Page page: pages) {
                analyseRecursive(page);
            }
            //ModelRepository.getInstance().destroyCurrentWorkspace();
            return petriNet;
        }
        else { //if (PNMLUtils.isHLPNDocument(rc)) {

            fr.lip6.move.pnml.hlpn.hlcorestructure.hlapi.PetriNetDocHLAPI ptDoc =
                    (fr.lip6.move.pnml.hlpn.hlcorestructure.hlapi.PetriNetDocHLAPI)rc;

            String id = ptDoc.getNets().get(0).getId();
            if (id.equals("color")) {
                // only process one petri net for testing
                coloredPetriNet = new ColoredPetriNet();
                coloredPetriNet.setId(id);

                List<fr.lip6.move.pnml.hlpn.hlcorestructure.Page> pages = ptDoc.getNets().get(0).getPages();
                for(fr.lip6.move.pnml.hlpn.hlcorestructure.Page page: pages) {
                    analyseColor(page);
                }
                //ModelRepository.getInstance().destroyCurrentWorkspace();
                return coloredPetriNet;
            } else {
                highLevelPetriNet = new HighLevelPetriNet();
                highLevelPetriNet.setId(id);
                List<fr.lip6.move.pnml.hlpn.hlcorestructure.Page> pages = ptDoc.getNets().get(0).getPages();
                for(fr.lip6.move.pnml.hlpn.hlcorestructure.Page page: pages) {
                    analyseHL(page);
                }

                //ModelRepository.getInstance().destroyCurrentWorkspace();
                return highLevelPetriNet;
            }
        }

    }

    // default no pages inside
    public void analyseRecursive(Page page) {
        List<PnObject> pnObjects = page.getObjects();
        for(PnObject pnObject: pnObjects) {
            if(pnObject.getClass() == PlaceImpl.class) {
                Place place = new Place((PlaceImpl) pnObject);
                String placeId = place.getId();
                HashMap<String, Place> placeMap = petriNet.getPlaces();
                placeMap.put(placeId, place);
                petriNet.setPlaces(placeMap);
            }
            if(pnObject.getClass() == TransitionImpl.class) {
                Transition transition = new Transition((TransitionImpl) pnObject);
                String transitionId = transition.getId();
                HashMap<String, Transition> transitionMap = petriNet.getTransitions();
                transitionMap.put(transitionId, transition);
                petriNet.setTransitions(transitionMap);
            }
            if(pnObject.getClass() == ArcImpl.class) {
                Edge edge = new Edge((ArcImpl) pnObject);
                ArrayList<Edge> edges = petriNet.getEdges();
                edges.add(edge);
                petriNet.setEdges(edges);
            }
            if(pnObject.getClass() == PageImpl.class) {
                analyseRecursive((Page)pnObject);
            }
        }
    }

    public void analyseColor(fr.lip6.move.pnml.hlpn.hlcorestructure.Page page) {
        List<Declaration> declarations = page.getDeclaration();
        for(Declaration declaration: declarations){
            List<TermsDeclaration> termsDeclarations = declaration.getStructure().getDeclaration();
            for(TermsDeclaration termsDeclaration: termsDeclarations){
                if(termsDeclaration.getClass() == VariableDeclImpl.class){
                    VariableDeclImpl variableDecl = (VariableDeclImpl) termsDeclaration;
                    Variable variable = new Variable(variableDecl);
                    HashMap<String, Variable> variableHashMap = coloredPetriNet.getVariables();
                    variableHashMap.put(variable.getId(), variable);
                    coloredPetriNet.setVariables(variableHashMap);
                }
            }
        }
        List<fr.lip6.move.pnml.hlpn.hlcorestructure.PnObject> pnObjects = page.getObjects();
        for(fr.lip6.move.pnml.hlpn.hlcorestructure.PnObject pnObject: pnObjects) {
            if(pnObject.getClass() == fr.lip6.move.pnml.hlpn.hlcorestructure.impl.PlaceImpl.class) {
                ColoredPlace coloredPlace = new ColoredPlace((fr.lip6.move.pnml.hlpn.hlcorestructure.impl.PlaceImpl) pnObject);
                HashMap<String, ColoredPlace> coloredPlaceHashMap = coloredPetriNet.getColoredPlaceHashMap();
                String placeId = pnObject.getId();
                coloredPlaceHashMap.put(placeId, coloredPlace);
                coloredPetriNet.setColoredPlaceHashMap(coloredPlaceHashMap);
            }

            if(pnObject.getClass() == fr.lip6.move.pnml.hlpn.hlcorestructure.impl.TransitionImpl.class) {
                ColoredTransition coloredTransition = new ColoredTransition((fr.lip6.move.pnml.hlpn.hlcorestructure.impl.TransitionImpl) pnObject);
                HashMap<String, ColoredTransition> coloredTransitionHashMap = coloredPetriNet.getColoredTransitionHashMap();
                String transitionId = pnObject.getId();
                coloredTransitionHashMap.put(transitionId, coloredTransition);
                coloredPetriNet.setColoredTransitionHashMap(coloredTransitionHashMap);
            }

            if(pnObject.getClass() == fr.lip6.move.pnml.hlpn.hlcorestructure.impl.ArcImpl.class) {
                ColoredEdge coloredEdge = new ColoredEdge((fr.lip6.move.pnml.hlpn.hlcorestructure.impl.ArcImpl) pnObject);
                ArrayList<ColoredEdge> coloredEdges = coloredPetriNet.getColoredEdges();
                coloredEdges.add(coloredEdge);
                coloredPetriNet.setColoredEdges(coloredEdges);
            }

            if(pnObject.getClass() == fr.lip6.move.pnml.hlpn.hlcorestructure.impl.PageImpl.class) {
                analyseColor((fr.lip6.move.pnml.hlpn.hlcorestructure.Page) pnObject);
            }
        }
    }

    public void analyseHL(fr.lip6.move.pnml.hlpn.hlcorestructure.Page page){
        List<Declaration> declarations = page.getDeclaration();
        for(Declaration declaration: declarations){
            List<TermsDeclaration> termsDeclarations = declaration.getStructure().getDeclaration();
            for(TermsDeclaration termsDeclaration: termsDeclarations){
                if(termsDeclaration.getClass() == VariableDeclImpl.class){
                    VariableDeclImpl variableDecl = (VariableDeclImpl) termsDeclaration;
                    Variable variable = new Variable(variableDecl);
                    HashMap<String, Variable> variableHashMap = highLevelPetriNet.getVariables();
                    variableHashMap.put(variable.getId(), variable);
                    highLevelPetriNet.setVariables(variableHashMap);
                }
            }
        }
        List<fr.lip6.move.pnml.hlpn.hlcorestructure.PnObject> pnObjects = page.getObjects();
        for(fr.lip6.move.pnml.hlpn.hlcorestructure.PnObject pnObject: pnObjects) {
            if(pnObject.getClass() == fr.lip6.move.pnml.hlpn.hlcorestructure.impl.PlaceImpl.class) {
                HighLevelPlace highLevelPlace = new HighLevelPlace((fr.lip6.move.pnml.hlpn.hlcorestructure.impl.PlaceImpl) pnObject);
                HashMap<String, HighLevelPlace> highLevelPlaceHashMap = highLevelPetriNet.getHighLevelPlaceHashMap();
                String placeId = pnObject.getId();
                highLevelPlaceHashMap.put(placeId, highLevelPlace);
                highLevelPetriNet.setHighLevelPlaceHashMap(highLevelPlaceHashMap);
            }

            if(pnObject.getClass() == fr.lip6.move.pnml.hlpn.hlcorestructure.impl.TransitionImpl.class) {
                HighLevelTransition highLevelTransition = new HighLevelTransition((fr.lip6.move.pnml.hlpn.hlcorestructure.impl.TransitionImpl) pnObject);
                HashMap<String, HighLevelTransition> highLevelTransitionHashMap = highLevelPetriNet.getHighLevelTransitionHashMap();
                String transitionId = pnObject.getId();
                highLevelTransitionHashMap.put(transitionId, highLevelTransition);
                highLevelPetriNet.setHighLevelTransitionHashMap(highLevelTransitionHashMap);
            }

            if(pnObject.getClass() == fr.lip6.move.pnml.hlpn.hlcorestructure.impl.ArcImpl.class) {
                HighLevelEdge highLevelEdge = new HighLevelEdge((fr.lip6.move.pnml.hlpn.hlcorestructure.impl.ArcImpl) pnObject);
                ArrayList<HighLevelEdge> highLevelEdges = highLevelPetriNet.getHighLevelEdges();
                highLevelEdges.add(highLevelEdge);
                highLevelPetriNet.setHighLevelEdges(highLevelEdges);
            }

            if(pnObject.getClass() == fr.lip6.move.pnml.hlpn.hlcorestructure.impl.PageImpl.class) {
                analyseHL((fr.lip6.move.pnml.hlpn.hlcorestructure.Page) pnObject);
            }
        }
    }

}
