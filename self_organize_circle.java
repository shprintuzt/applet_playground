import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.Math;
import java.util.Random;
import java.awt.Image;

/*
<applet code="self_organize_circle.class" width="900" height="1000">
</applet>
*/

public class self_organize_circle extends Applet implements Runnable{
  Thread thread = null;
  
  int n = 50;
  
  int a = 8;
  int b = 80;
  
  Random r = new Random();
  
  double dx[] = new double [50];
  double dy[] = new double [50];
  
  double dxtmp[] = new double [50];
  double dytmp[] = new double [50];
  
  double d[] = new double [100];
  
  public void init(){
    //random initial configuration
    for(int i=0;i<n;i++){
      dx[i] = 650;//(double)r.nextInt(800)+100;
      dxtmp[i] = dx[i];
      dy[i] = 700;//(double)r.nextInt(800)+100;
      dytmp[i] = dy[i];
    }
    //initial configuration of 4 nodes where they cannot form square
/*    dx[0] = 100; dx[1] = 200; dx[2] = 200; dx[3] = 100;
    dy[0] = 100; dy[1] = 200; dy[2] = 100; dy[3] = 200;*/
    
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
      for(int j=1;j<=n/2;j++){
        buffer.drawLine((int)dx[i]+10,(int)dy[i]+10,
                   (int)dx[(i+j)%n]+10,(int)dy[(i+j)%n]+10);
      }
    }
    
    g.drawImage(back,0,0,this);
  }

  public void move(int i){
    
    int i_minus = (i+n-1)%n;
    int i_plus = (i+1)%n;
    
/*    //the opposite point of parallelogram
    int dx1 = dx[i_minus]-dx[i]+dx[i_plus];
    int dy1 = dy[i_minus]-dy[i]+dy[i_plus];
*/    
    //distance of i-1(d1) and i+1(d2)

    for(int j=0;j<n;j++){
      d[j] = Math.sqrt(Math.pow(dx[i]-dx[j],2)+Math.pow(dy[i]-dy[j],2));
      if ( d[j] == 0 )  d[j] = 1;
    }

/*    //the separating point of i-1 and i+1 by the ratio of d1 to d2
    int dx2 = (d1*dx[i_plus]+d2*dx[i_minus])/(d1+d2);
    int dy2 = (d1*dy[i_plus]+d2*dy[i_minus])/(d1+d2);
    
    //distance of separating point
    int d3 = (int)Math.sqrt(Math.pow(dx[i]-dx2,2)+Math.pow(dy[i]-dy2,2));
*/    
    dxtmp[i] = dx[i] + ((dx[i_minus]-dx[i]) + (dx[i_plus]-dx[i]))/a;
    dytmp[i] = dy[i] + ((dy[i_minus]-dy[i]) + (dy[i_plus]-dy[i]))/a;

    for(int j=0;j<n;j++){
      if(j != i){
        if ( dx[j]-dx[i] == 0 ) {
          dxtmp[i] -= (r.nextInt(2)/Math.pow(d[j],2))*a;
        } else {
          dxtmp[i] -= ((dx[j]-dx[i])/Math.pow(d[j],2))*a;
        }
        if ( dx[j]-dx[i] == 0 ) {
          dytmp[i] -= (r.nextInt(2)/Math.pow(d[j],2))*a;
        } else {
          dytmp[i] -= ((dy[j]-dy[i])/Math.pow(d[j],2))*a;
        }
      }
    }



    
/*    dxtmp[i_minus] += (dx[i]-dx[i_minus])/10;
    dytmp[i_minus] += (dy[i]-dy[i_minus])/10;
    dxtmp[i_plus] += (dx[i]-dx[i_plus])/10;
    dytmp[i_plus] += (dy[i]-dy[i_plus])/10;
*/    
    
  }

/*  public void update(Graphics g){
    paint(g);
  }
*/
  public void run(){
    while(true){
      for(int i=0;i<n;i++)
        move(i);
      for(int i=0;i<n;i++){
        dx[i] = dxtmp[i];
        dy[i] = dytmp[i];
      }
      
      repaint();
      
      try{
        Thread.sleep(10);
      }catch (InterruptedException e){
      }
    }
  }
}