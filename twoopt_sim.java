import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.Math;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
<applet code="twoopt_sim.class" width="900" height="1000">
</applet>
*/

public class twoopt_sim extends Applet implements KeyListener{
  Random r = new Random();
  
  public static final int N = 10;//the number of nodes
  public static final int M = 5;//the number of agents
  
  int a = 200;//the radius of the ring
  
  int dx[] = new int [N];
  int dy[] = new int [N];
  
  int isAgent[] = new int [N];//whether there are agents in node i,0:no agent,1:one agent,2:two agents
  int isAgent_tmp[] = new int [N];
  
  boolean isSearched[] = new boolean [N];
  boolean isSearched_tmp[] = new boolean [N];
  
  int turn = 0;//which component move, agent or adversary
  
  int steps = 0;
  
  public void init(){    
    for(int i=0;i<N;i++){
      dx[i] = (int)(-a*Math.sin(i*2*Math.PI/N)+400);
      dy[i] = (int)(-a*Math.cos(i*2*Math.PI/N)+400);
      isAgent[i] = 0;
      isSearched[i] = false;
    }
    
    isAgent[N/2] = M;
    
    for(int i=0;i<N;i++){
      isAgent_tmp[i] = isAgent[i];
    }
    
    addKeyListener(this);
  }

  public void paint(Graphics g){
    
    for(int i=0;i<N;i++){
      if(isAgent[i] == 0){
        if(isSearched[i] == true) g.setColor(Color.gray);
        else g.setColor(Color.black);
      } else {
        if(i == 0){g.setColor(Color.green);
        } else if(isAgent[i] == 1){g.setColor(Color.blue);
        } else if(isAgent[i] >2){g.setColor(Color.cyan);
        } else {g.setColor(Color.red);}
      }
      
      g.fillOval((int)dx[i],(int)dy[i],30,30);
    }
    
    requestFocusInWindow();
    
  }

/*  public void next_sim(int i){
    
  }
*/
  public void move_agent(int i){
    
    int i_minus = (i+N-1)%N;
    int i_plus = (i+1)%N;
    
    if(isAgent[i] == 1){
      if((isAgent[i_minus] >= 1)&&(isAgent[i_plus] == 0)){
        isAgent_tmp[i]--;
        isAgent_tmp[i_plus]++;
      } else if((isAgent[i_minus] == 0)&&(isAgent[i_plus] >= 1)){
        isAgent_tmp[i]--;
        isAgent_tmp[i_minus]++;
      } else if((isAgent[i_minus] == 0)&&(isAgent[i_plus] == 0)){
        if((isSearched[i_minus] == true)&&(isSearched[i_plus] == false)){
          isAgent_tmp[i]--;
          isAgent_tmp[i_plus]++;
        } else if((isSearched[i_minus] == false)&&(isSearched[i_plus] == true)){
          isAgent_tmp[i]--;
          isAgent_tmp[i_minus]++;
        } else {
          int dir = r.nextInt(2);
          if(dir == 0){
            isAgent_tmp[i]--;
            isAgent_tmp[i_plus]++;
          } else {
            isAgent_tmp[i]--;
            isAgent_tmp[i_minus]++;
          }
        }
      }
    } else if(isAgent[i] >= 2){
      if((isAgent[i_minus] >= 1)&&(isAgent[i_plus] == 0)){
        isAgent_tmp[i]--;
        isAgent_tmp[i_plus]++;
      } else if((isAgent[i_minus] == 0)&&(isAgent[i_plus] >= 1)){
        isAgent_tmp[i]--;
        isAgent_tmp[i_minus]++;
      } else if((isAgent[i_minus] == 0)&&(isAgent[i_plus] == 0)){
        isAgent_tmp[i]-=2;
        isAgent_tmp[i_plus]++;
        isAgent_tmp[i_minus]++;
      }
    }    
  }

  public void move_adversary(){
    int dis_r = 1;
    int dis_l = 1;
    
    int i = 1;
    while((isAgent[i] == 0)&&(dis_r < N/2+1)){
      dis_r++;
      i++;
    }
    
    int j = N-1;
    while((isAgent[j] == 0)&&(dis_l < N/2+1)){
      dis_l++;
      j--;
    }
    
    int dis_max = 0;
    int dis_tmp = 0;
    int swap_node = 0;
    if(dis_r <= dis_l){
      while(i < j){
        i++;
        if(isAgent[i] == 0){
          dis_tmp++;
        } else {
          if(dis_max < dis_tmp+1){
            dis_max = dis_tmp+1;
            swap_node = i;
          }
          dis_tmp = 0;
        }
      }
      
      if(dis_max > 1){
        for(int k=dis_r;k<swap_node;k++){
          isAgent_tmp[k] = isAgent[swap_node+dis_r-k-1];
          isSearched_tmp[k] = isSearched[swap_node+dis_r-k-1];
        }
      } else if((dis_max < dis_l/2)&&(dis_r < dis_l/2)){
        for(int k=1;k<N-dis_l/2;k++){
          isAgent_tmp[k] = isAgent[N-dis_l/2-k];
          isSearched_tmp[k] = isSearched[N-dis_l/2-k];
        }
      }
      
    } else {
      while(i < j){
        j--;
        if(isAgent[j] == 0){
          dis_tmp++;
        } else {
          if(dis_max < dis_tmp+1){
            dis_max = dis_tmp+1;
            swap_node = j;
          }
          dis_tmp = 0;
        }
      }
      
      if(dis_max > 1){
        for(int k=N-dis_l;k>swap_node;k--){
          isAgent_tmp[k] = isAgent[swap_node+N-dis_l-k+1];
          isSearched[k] = isSearched[swap_node+N-dis_l-k+1];
        }
      } else if((dis_max < dis_r/2)&&(dis_l < dis_r/2)){
        for(int k=N-1;k>dis_r/2;k--){
          isAgent_tmp[k] = isAgent[dis_r/2+N-k];
          isSearched[k] = isSearched[dis_r/2+N-k];
        }
      }
      
    }
    
    
  }

  public void keyPressed(KeyEvent e){
    if(turn == 0){
      move_adversary();
      for(int i=0;i<N;i++){
        isAgent[i] = isAgent_tmp[i];
      }
      turn = 1;
    } else {
      for(int i=0;i<N;i++)
        move_agent(i);
      for(int i=0;i<N;i++){
        isAgent[i] = isAgent_tmp[i];
        if(isAgent[i] == 1){
          isSearched[i] = true;
        }
      }
      
      turn = 0;
    }
    
    int m = 0;
    int m_max = 0;
    int searched = 0;
    for(int i=0;i<N;i++){
      if(m_max < isAgent[i]) m_max = isAgent[i];
      m+=isAgent[i];
      if(isSearched[i] == true) searched++;
    }
    System.out.println(m+";"+m_max+":"+searched+";"+steps);
    
    repaint();
    
  }

  public void keyReleased(KeyEvent e){
  
  }

  public void keyTyped(KeyEvent e){
  
  }
}