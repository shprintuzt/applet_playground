import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.Math;
import java.util.Random;
import java.awt.Image;

/*
<applet code="graph.class" width="900" height="1000">
</applet>
*/


public class graph extends Applet implements Runnable {
  Thread thread = null;
  
  static final int n = 10;
  
  int a = 20;
  int b = 80;
  
  int range = 100;
  int thre = 50;
  
  Random r = new Random();
  
  double dx[] = new double [n];
  double dy[] = new double [n];
  
  double dxtmp[] = new double [n];
  double dytmp[] = new double [n];
  
  double d[][] = new double [n][n];
  
  boolean link[][] = new boolean [n][n];
  
  public void init(){
    //random initial configuration
    for(int i=0;i<n;i++){
      dx[i] = r.nextInt(800)+100;
      dy[i] = r.nextInt(800)+100;
    }
    
    //grid
    /*
    for(int i=0;i<49;i++){
      dx[i] = 20*(i%7)+400;
      dxtmp[i] = dx[i];
    }
    for(int i=0;i<7;i++){
      for(int j=0;j<7;j++){
        dy[7*i+j] = 20*i+400;
        dytmp[i] = dy[i];
      }
    }*/
    
    //initial configuration of 4 nodes where they cannot form square
    /*
    dx[0] = 100; dx[1] = 200; dx[2] = 200; dx[3] = 100;
    dy[0] = 100; dy[1] = 200; dy[2] = 100; dy[3] = 200;*/
    
    //random graph
    /*
    for(int i=0;i<n;i++)
    for(int j=i;j<n;j++){
      if ( r.nextInt(range) < thre ) {
        link[i][j] = true;
        link[j][i] = true;
      }
    }*/
    
    //line graph
    /*
    for(int i=0;i<n-1;i++){
      link[i][i+1] = true;
      link[i+1][i] = true;
    }*/
    
    //ring graph
    /*
    for(int i=0;i<n;i++){
      link[i][(i+1)%n] = true;
      link[(i+1)%n][i] = true;
    }*/
    
    //grid graph
    /*
    int nroot = (int)Math.sqrt(n);
    for(int i=0;i<nroot;i++)
    for(int j=0;j<nroot-1;j++){
      link[i*nroot+j][i*nroot+(j+1)] = true;
      link[i*nroot+(j+1)][i*nroot+j] = true;
    }
    for(int i=0;i<nroot-1;i++)
    for(int j=0;j<nroot;j++){
      link[i*nroot+j][(i+1)*nroot+j] = true;
      link[(i+1)*nroot+j][i*nroot+j] = true;
    }
    */
    
    //torus graph
    /*
    int nroot = (int)Math.sqrt(n);
    for(int i=0;i<nroot;i++)
    for(int j=0;j<nroot;j++){
      link[i*nroot+j][i*nroot+((j+1)%nroot)] = true;
      link[i*nroot+((j+1)%nroot)][i*nroot+j] = true;
      link[i*nroot+j][((i+1)%nroot)*nroot+j] = true;
      link[((i+1)%nroot)*nroot+j][i*nroot+j] = true;
    }*/
    
    thread = new Thread(this);
    thread.start();
  }

  public void update(Graphics g){
    paint(g);
  }

  public void paint(Graphics g){
    Dimension size = getSize();
    Image back = createImage(size.width,size.height);
    Graphics buffer = back.getGraphics();
    
    int color_tr = 255/n;

    buffer.setColor(new Color(0,0,0));
    buffer.fillRect(0,0,size.width,size.height);
    for(int i=0;i<n;i++){
//      buffer.setColor(new Color(i*color_tr,i*color_tr,i*color_tr));
      buffer.setColor(new Color(0,0,255));
      buffer.fillOval((int)dx[i],(int)dy[i],20,20);
      for(int j=0;j<n;j++){
        if ( link[i][j] == true ) {
          buffer.drawLine((int)dx[i]+10,(int)dy[i]+10,
                   (int)dx[j]+10,(int)dy[j]+10);
        }
      }
    }
    
    g.drawImage(back,0,0,this);
  }

  public void move(int i){
    
/*    //the opposite point of parallelogram
    int dx1 = dx[i_minus]-dx[i]+dx[i_plus];
    int dy1 = dy[i_minus]-dy[i]+dy[i_plus];
*/    
    //distance of i-1(d1) and i+1(d2)
    
    for(int j=0;j<n;j++){
      d[i][j] = Math.sqrt(Math.pow(dx[i]-dx[j],2)+Math.pow(dy[i]-dy[j],2));
      if ( d[i][j] == 0 ) {
        d[i][j] = 1;
      }
    }
/*    //the separating point of i-1 and i+1 by the ratio of d1 to d2
    int dx2 = (d1*dx[i_plus]+d2*dx[i_minus])/(d1+d2);
    int dy2 = (d1*dy[i_plus]+d2*dy[i_minus])/(d1+d2);
    
    //distance of separating point
    int d3 = (int)Math.sqrt(Math.pow(dx[i]-dx2,2)+Math.pow(dy[i]-dy2,2));
*/    
    dxtmp[i] = dx[i];
    dytmp[i] = dy[i];
    
    for(int j=0;j<n;j++){
      if ( link[i][j] == true ) {
        dxtmp[i] += (dx[j]-dx[i])/a;
        dytmp[i] += (dy[j]-dy[i])/a;
      }
    }
    
    for(int j=0;j<n;j++){
      if(j != i){
        if ( dx[j] - dx[i] == 0 ) {
          dxtmp[i] -= (r.nextInt(2)/Math.pow(d[i][j],2))*a;
        } else {
          dxtmp[i] -= ((dx[j]-dx[i])/Math.pow(d[i][j],2))*a;
        }
        if ( dy[j] - dy[i] == 0 ) {
          dytmp[i] -= (r.nextInt(2)/Math.pow(d[i][j],2))*a;
        } else {
          dytmp[i] -= ((dy[j]-dy[i])/Math.pow(d[i][j],2))*a;
        }
      }
    }
    
/*    dxtmp[i_minus] += (dx[i]-dx[i_minus])/10;
    dytmp[i_minus] += (dy[i]-dy[i_minus])/10;
    dxtmp[i_plus] += (dx[i]-dx[i_plus])/10;
    dytmp[i_plus] += (dy[i]-dy[i_plus])/10;
*/    
    
  }

  public void run(){
    while(true){
/*      for(int i=0;i<n;i++)
        move(i);
      for(int i=0;i<n;i++){
        dx[i] = dxtmp[i];
        dy[i] = dytmp[i];
      }
*/      
      repaint();
      
      try{
        Thread.sleep(1);
      }catch (InterruptedException e){
      }
    }
  }
}