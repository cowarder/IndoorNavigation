package com.example.asus.opticalpath;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    public static int [][]t={{0,1,0,1,0},{0,1,1,1,1},{0,1,0,1,1},{1,1,1,1,0},{0,1,0,1,1}};//输入可达性方格

    public static Vertex[] Bag;         //可达表格组成的数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeWeightGraph graph=new EdgeWeightGraph();        //构造表格
    }

    public void main(String args[]){
        calAdjacent();

    }

    public void calAdjacent(){
        for(Vertex item:Bag){
            int num=0;
            //如果在可达方格内找到与其相邻的方格，就将方格添加到邻接数组中
            for(Vertex p:Bag){
                if(p!=item&&((p.xCoordinate==item.xCoordinate-1&&p.yCoordinate==item.yCoordinate)
                        ||(p.xCoordinate==item.xCoordinate+1&&p.yCoordinate==item.yCoordinate)
                        ||(p.xCoordinate==item.xCoordinate&&p.yCoordinate==item.yCoordinate-1)
                        ||(p.xCoordinate==item.xCoordinate&&p.yCoordinate==item.yCoordinate+1))){
                    num++;
                }
            }
            item.adjVertex=new Vertex[num];
        }

    }


}
