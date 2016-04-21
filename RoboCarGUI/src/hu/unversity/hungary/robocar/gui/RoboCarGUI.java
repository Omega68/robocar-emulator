package hu.unversity.hungary.robocar.gui;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RoboCarGUI extends Application{

	private Label osmName;
	private TextField osmField;
	private Label cityName;
	private TextField cityField;
	private Label gangstersLabel;
	private TextField gangtersField;
	private Label portLabel;
	private TextField portField;
	private Label teamLabel;
	private TextField teamField;
    private static Button btn = new Button();
	
	    public static void main(String[] args) {
	        launch(args);
	    }
	
	private void initGui(){
		this.osmName=new Label("Osm name");
		this.osmField=new TextField("map");
		this.osmField.setPromptText("Name of the OSM file");
		this.cityName=new Label("City name");
		this.cityField=new TextField("Budapest");
		this.cityField.setPromptText("Name of the city");
		this.gangstersLabel=new Label("Gangsters");
		this.gangtersField=new TextField("200");
		this.gangtersField.setPromptText("Number of Gangsters");
		this.portLabel=new Label("Port");
		this.portField=new TextField("10007");
		this.portField.setPromptText("Set the port value");
		this.teamLabel=new Label("Team");
		this.teamField=new TextField("BudapestPolice");
		this.teamField.setPromptText("Name of the team");
	}
	    
	private static class NewProcess implements Runnable{

		private String osmField;
		private String cityField;
		private String gangtersField;
		private String portField;
		private String teamField;
		
		public NewProcess(String osmField, String cityField, String gangtersField, String portField, String teamField) {
			super();
			this.osmField = osmField;
			this.cityField = cityField;
			this.gangtersField = gangtersField;
			this.portField = portField;
			this.teamField = teamField;
		}

		@Override
		public void run() {
			  Process proc=null;
              Process proc2=null;
              Process proc3=null;
              Process proc4=null;
              Process proc5=null;
              try{
            	  btn.setDisable(true);
             // String[] commandStart={"xterm","-e","cd; cd robocar/robocar-emulator/justine/rcemu; src/smartcity --osm=../map.osm --city=Budapest --shm=BudapestSharedMemory --node2gps=../budapest-lmap.txt"};      
              String[] commandStart={"xterm","-e","cd; cd robocar/robocar-emulator/justine/rcemu; src/smartcity --osm=../"+osmField+".osm --city="+cityField+" --shm="+cityField+"SharedMemory --node2gps=../"+cityField.toLowerCase()+"-lmap.txt"};      
              proc=new ProcessBuilder(commandStart).start();
              System.out.println("Process was successfully started:"+proc.isAlive());
              Thread.sleep(20000);
              String[] command2Start={"xterm","-e","cd; cd robocar/robocar-emulator/justine/rcemu; src/traffic --port="+portField+" --shm="+cityField+"SharedMemory"};
              proc2=new ProcessBuilder(command2Start).start();
              Thread.sleep(10000);
              
              String[] command3Start={"xterm","-e","cd; cd robocar/robocar-emulator/justine/rcwin; java -jar target/site/justine-rcwin-0.0.16-jar-with-dependencies.jar ../"+cityField.toLowerCase()+"-lmap.txt "};
              proc3=new ProcessBuilder(command3Start).start();
              Thread.sleep(5000);
              String[] command4Start={"xterm","-e","(sleep 1; echo \"<init Norbi "+gangtersField+" g>\"; sleep 1)|telnet localhost "+portField};
              proc4=new ProcessBuilder(command4Start).start();
              
              Thread.sleep(5000);
              String[] command5Start={"xterm","-e","cd; cd robocar/robocar-emulator/justine/rcemu; src/samplemyshmclient --port="+portField+" --shm="+cityField+"SharedMemory --team="+teamField};
              proc5=new ProcessBuilder(command5Start).start();
              
              btn.setDisable(false);
              
              }catch(Exception exc){
              	System.out.println("Exception occured:"+exc.getMessage());
              }
              finally{
                  btn.setDisable(false);
              	System.out.println("Process was successfully stopped:"+!proc.isAlive());
              }
		}
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("RoboCar Interface");
        //initialization
        initGui();
        
        btn.setText("Start");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 580, 200);
        grid.add(osmName, 0, 0);
        grid.add(osmField, 0, 1);
        grid.add(cityName, 1, 0);
        grid.add(cityField, 1, 1);
        grid.add(gangstersLabel, 2, 0);
        grid.add(gangtersField, 2, 1);
        grid.add(portLabel, 3, 0);
        grid.add(portField, 3, 1);
        grid.add(teamLabel, 4, 0);
        grid.add(teamField, 4, 1);
        grid.add(btn, 0, 3);
        primaryStage.setScene(scene);
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                Thread r=new Thread(new NewProcess(osmField.getText(), cityField.getText(), gangtersField.getText(), portField.getText(), teamField.getText()));
                r.start();

            }
        });
        
        primaryStage.show();
	}

}
