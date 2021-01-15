package agents;

import containers.ConsomateurContainer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;

public class ConsomateurAgent extends GuiAgent {
    //interface graphique
    private ConsomateurContainer gui;

    @Override
    protected void setup() {
        if(getArguments().length==1){
            gui = (ConsomateurContainer) getArguments()[0];
            gui.setConsomateurAgent(this);
        }
        System.out.println("Creation et initialisation de l'agent: "+this.getAID().getName());

        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);

        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage = receive();
                if(aclMessage != null){

                    switch(aclMessage.getPerformative()) {
                        case ACLMessage.CONFIRM:
                            gui.logMessage(aclMessage);
                            break;

                        default:
                            break;
                    }
                }else block();
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("L'agent : "+this.getAID().getName()+" Taken down!!! ");
    }

    @Override
    protected void beforeMove() {
        try {
            System.out.println("Avant migration de l'agent: "+this.getAID().getName());
            System.out.println("de conteneur: "+this.getContainerController().getContainerName());
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void afterMove() {
        try {
            System.out.println("Apres migration de l'agent: "+this.getAID().getName());
            System.out.println("vers conteneur: "+this.getContainerController().getContainerName());
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        //type achat si 1 q'on a passer on parameter dans class ConsomaContainer - button.setOnAction
        if(guiEvent.getType()==1){
            //achat
            //MESSAGE Type est request
            ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
            String livre = guiEvent.getParameter(0).toString();
            aclMessage.setContent(livre);
            aclMessage.addReceiver(new AID("ACHETEUR", AID.ISLOCALNAME));
            send(aclMessage);

        }
    }


}
