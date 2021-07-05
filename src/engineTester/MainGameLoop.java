package engineTester;

import Models.TexturedModel;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import engineTester.audio.Audio;
import engineTester.audio.State;
import entities.*;
import guis.GuiRenderer;
import guis.GuiTexture;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import Models.RawModel;
import renderEngine.MasterRenderer;
import terrains.Terrain;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {
    public static void main(String[] args){

        Audio.run();


        DisplayManager.createDisplay();
        Loader loader = new Loader();

        ModelData bodyData = OBJFileLoader.loadOBJ( "/ball");
        RawModel bodyRawModel = loader.loadToVAO(bodyData.getVertices(),bodyData.getTextureCoords(),bodyData.getNormals(),bodyData.getIndices());
        TexturedModel bodyModel= new TexturedModel(bodyRawModel,new ModelTexture(loader.loadTexture("/ball")).setHasTransparency(false).setShineDamper(100).setReflectivity(1));
        Player golfBall = new Player(bodyModel,new Vector3f(5000,1,5000),1,0,0,0);



        ModelData grassData = OBJFileLoader.loadOBJ("/environment/grass");
        RawModel grassModel = loader.loadToVAO(grassData.getVertices(),grassData.getTextureCoords(),grassData.getNormals(),grassData.getIndices());
        ModelData fernData = OBJFileLoader.loadOBJ("/environment/fern");
        RawModel fernModel = loader.loadToVAO(fernData.getVertices(),fernData.getTextureCoords(),fernData.getNormals(),fernData.getIndices());
        ModelData lowPolyTreeData = OBJFileLoader.loadOBJ("/environment/lowPolyTree");
        RawModel lowPolyTreeModel = loader.loadToVAO(lowPolyTreeData.getVertices(),lowPolyTreeData.getTextureCoords(),lowPolyTreeData.getNormals(),lowPolyTreeData.getIndices());


        TexturedModel fernTexturedModel = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("/environment/fern")));
        TexturedModel grassTexturedModel = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("/environment/grass")));
        TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,new ModelTexture(loader.loadTexture("/environment/lowPolyTree")));

        grassTexturedModel.getTexture().setHasTransparency(true);
        grassTexturedModel.getTexture().setHasFakeLighting(true);
        fernTexturedModel.getTexture().setHasTransparency(true);
       // Entity[] fern = new Entity[1000];
  //      Entity[] grass = new Entity[2000];
        Entity[] lowPolyTree = new Entity[100];
        Random random = new Random();
   //     for(int i=0;i<2000;i++){
   //         grass[i] = new Entity(grassTexturedModel, new Vector3f(random.nextInt(780)+10,0,random.nextInt(780)+10),1,0,0,0);

   //     }
        for(int i=0;i<100;i++) {
            lowPolyTree[i] = new Entity(lowPolyTreeTexturedModel, new Vector3f(random.nextInt(780)+10,0,random.nextInt(780)+10),0.5f,0,0,0);
        }

  //      for(int i=0;i<1000;i++){
   //         fern[i] = new Entity(fernTexturedModel, new Vector3f(random.nextInt(780)+10,0,random.nextInt(780)+10),1,0,0,0);
   //     }

        Light light = new Light(new Vector3f(0,1000000,0), new Vector3f(1,1,1));



        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("/terrain/grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("/terrain/grass"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("/terrain/grass"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("/terrain/grass"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("/terrain/blendMap"));

        Terrain terrain = new Terrain(0,0,loader,texturePack,blendMap,"/terrain/heightMap");


        Camera camera = new Camera(golfBall);

        MasterRenderer renderer = new MasterRenderer();

        Audio.play(State.background);

        GuiRenderer guiRenderer = new GuiRenderer(loader);
        List<GuiTexture> guis = new ArrayList<>();
       // GuiTexture guiTexture = new GuiTexture(loader.loadTexture("ball"), new Vector2f(0.5f,0.5f),new Vector2f(0.25f,0.25f));
      //  guis.add(guiTexture);

        while(!Display.isCloseRequested()){


            camera.move();

            golfBall.move();



                renderer.processTerrain(terrain);

            renderer.processEntity(golfBall);
      //      for(Entity grassObject:grass){
     //           renderer.processEntity(grassObject);
     //       }
         //  for(Entity fernObject:fern){
       //         renderer.processEntity(fernObject);
      //      }
            for(Entity lowPolyTreeObject:lowPolyTree){
                renderer.processEntity(lowPolyTreeObject);
            }

            renderer.render(light,camera);
            guiRenderer.render(guis);

            DisplayManager.updateDisplay();
        }
        guiRenderer.cleanUP();
        Audio.cleanUp();
        renderer.cleanUP();
        loader.cleanUP();
        DisplayManager.closeDisplay();

    }
}
