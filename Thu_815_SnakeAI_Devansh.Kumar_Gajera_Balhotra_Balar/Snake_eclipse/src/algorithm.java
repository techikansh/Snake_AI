import java.util.ArrayList;
import static java.lang.Math.abs;



public class algorithm{
    int snake_x, snake_y;

    int number_of_apple;
    int apple_x_coordinate;
    int apple_y_coordinate;
    ArrayList<Snakebody> snakes;
    ArrayList<Snakebody> snakes2;
    ArrayList<neighbors> neigh = new ArrayList<neighbors>();
    int score  = 0;
    ArrayList<neighbors> list = new ArrayList<neighbors>();
    ArrayList<neighbors> final_list = new ArrayList<neighbors>();
    int [] a1 = {25,-25,0,0};
    int []a2 = {0,0,25,-25};



    public algorithm(ArrayList snakes,ArrayList snakes2 ,int apple_x_coordinate, int apple_y_coordinate , int snake_x, int snake_y, int number_of_apple){
        this.snakes = snakes;
        this.snakes2 = snakes2;
        this.apple_x_coordinate = apple_x_coordinate;
        this.apple_y_coordinate = apple_y_coordinate;
        this.snake_x = snake_x;
        this.snake_y = snake_y;
        this.number_of_apple = number_of_apple;
    }

    public char find_neighbors(int snake_x, int snake_y){

        char dir;
        for(int i = 0; i<4; i++){
            //System.out.println(snake_y);
            neighbors n = new neighbors((snake_x+a1[i]),(snake_y+a2[i]));
            //neigh.add(n);
            if(is_neighbor(n)){
                if(is_free(n)){
                    n.distance = (manhatten_Distance(n.getNeighbors_x(),apple_x_coordinate,n.getNeighbors_y(),apple_y_coordinate))/25;
                    neigh.add(n);
                }
            }
        }

        // for(int i = 0; i <neigh.size();i++){
        //     System.out.println(neigh.get(i).getNeighbors_x());
        //     System.out.println(neigh.get(i).getNeighbors_y());
        //     //System.out.println(neigh.get(i).getTotal_free_neighbors());
        //     //System.out.println(neigh.get(i).getDistance());
        // }

        for(int i = 0 ; i<neigh.size(); i++){
            for(int j = 0; j<4; j++){
                neighbors add_on = new neighbors((neigh.get(i).getNeighbors_x())+a1[j],(neigh.get(i).getNeighbors_y())+a2[j]);
                if(is_neighbor(add_on)){
                    if(is_free(add_on)){
                        score = score +1;
                    }
                }

            }
            neigh.get(i).total_free_neighbors = score;

            score = 0;
        }
        //for(int i = 0; i <neigh.size();i++){
        //    System.out.println(" x "+neigh.get(i).getNeighbors_x());
        //    System.out.println(" y  "+neigh.get(i).getNeighbors_y());
        //    System.out.println(" n  "+neigh.get(i).getTotal_free_neighbors());
        //    System.out.println(" d "+neigh.get(i).getDistance());
        //}



        int max = 0;
        int min = 100;
        for(int i = 0; i< neigh.size() ; i++){
            if(neigh.get(i).getDistance() < min){
                min = neigh.get(i).getDistance();
            }
        }
        for(int i = 0; i< neigh.size() ; i++){
            if(neigh.get(i).getDistance() == min){
                list.add(neigh.get(i));
            }
        }

        for(int i = 0; i< neigh.size(); i++){
            if(neigh.get(i).getTotal_free_neighbors() > max){
                max = neigh.get(i).total_free_neighbors;
            }
        }

        for(int i = 0 ; i<neigh.size();i++){
            if(neigh.get(i).getTotal_free_neighbors() == max){
                final_list.add(neigh.get(i));
            }
        }

       if(list.get(0).getTotal_free_neighbors() >=2 || list.get(0).getDistance() <=2){
          dir = direction_call(list.get(0).getNeighbors_x(),list.get(0).getNeighbors_y());
      }else dir = direction_call(final_list.get(0).getNeighbors_x(),final_list.get(0).getNeighbors_y());

      // if(list.get(0).getNeighbors_x() == final_list.get(0).getNeighbors_x()  ||list.get(0).getDistance() <=1){
      //     dir = direction_call(list.get(0).getNeighbors_x(),list.get(0).getNeighbors_y());
      // }else dir = direction_call(final_list.get(0).getNeighbors_x(),final_list.get(0).getNeighbors_y());

     // if(list.size()> 1){
     //     for(int i = 0; i< list.size(); i++){
     //         if(list.get(i).getTotal_free_neighbors() > max){
     //             max = list.get(i).total_free_neighbors;
     //         }
     //     }

     //     for(int i = 0 ; i<list.size();i++){
     //         if(list.get(i).getTotal_free_neighbors() == max){
     //             final_list.add(list.get(i));
     //         }
     //     }

     //     dir = direction_call(final_list.get(0).getNeighbors_x(),final_list.get(0).getNeighbors_y());

     //
     // }else dir = direction_call(list.get(0).getNeighbors_x(),list.get(0).getNeighbors_y());




        //   for(int i = 0 ; i< list.size(); i++){
        //       if(list.get(i).getDistance() < min){
        //           min = list.get(i).getDistance();
        //       }
        //   }
        //   for(int i = 0 ; i< list.size(); i++){
        //       if(list.get(i).getDistance()  == min){
        //           //final_list.add(list.get(i));
        //           x_1 = list.get(i).getNeighbors_x();
        //           y_1 = list.get(i).getNeighbors_y();
        //           break;
        //       }
        //   }
        //  //x_1 =  final_list.get(0).getNeighbors_x();
        //  //y_1 = final_list.get(0).getNeighbors_y();




        neigh.clear();
        list.clear();
        final_list.clear();

        return dir;

    }

    public char direction_call(int x, int y){

        if(x == snake_x+25 && y == snake_y){
            return 'R';
        }else if(x == snake_x - 25 && y == snake_y){
            return 'L';
        }else if(x == snake_x && y == snake_y+25){
            return 'D';

        }else return 'U';
    }


    public boolean is_free(neighbors n){
        int a = 0, a2 = 0;
        for(int i = 0; i< snakes.size(); i++){
            if(n.getNeighbors_x() == snakes.get(i).getSnake_x_coordinate() && n.getNeighbors_y() == snakes.get(i).getSnake_y_coordinate()) {
                a = a+1;
            }
        }for(int i = 0; i< snakes2.size(); i++){
            if(n.getNeighbors_x() == snakes2.get(i).getSnake_x_coordinate() && n.getNeighbors_y() == snakes2.get(i).getSnake_y_coordinate()) {
                a2 = a2+1;
            }
        }

        if(a == 0 && a2== 0){
            return true;
        }else return false;
    }

    public int manhatten_Distance(int x1, int x2, int y1, int y2){

        return(abs(x1-x2)+ abs(y1 - y2));
    }

    public boolean is_neighbor(neighbors n){

        if(n.getNeighbors_x() >= 0 && n.getNeighbors_y()>=0 && n.getNeighbors_x()<600 && n.getNeighbors_y()< 600){
            return true;
        }else return false;
    }


}

//class parent_neighbors{
//    int parent_x;
//    int parent_y;
//
//    public parent_neighbors(int parent_x,int parent_y){
//        this.parent_x = parent_x;
//        this.parent_y = parent_y;
//    }
//
//    public int getParent_x() {
//        return parent_x;
//    }
//
//    public int getParent_y() {
//        return parent_y;
//    }
//}

class neighbors{

    int neighbors_x;
    int neighbors_y;
    int distance = 0;
    int total_free_neighbors = 0;
    int parent_x,parent_y;


    public void setParent_x(int parent_x){
        this.parent_x = parent_x;
    }

    public void setParent_y(int parent_y) {
        this.parent_y = parent_y;
    }

    public neighbors(int neighbors_x, int neighbors_y){
        this.neighbors_x = neighbors_x;
        this.neighbors_y = neighbors_y;
    }
    public int getNeighbors_x(){return neighbors_x;}
    public int getNeighbors_y(){return neighbors_y;}
    public int getDistance(){return distance;}
    public int getTotal_free_neighbors(){return total_free_neighbors;}
}