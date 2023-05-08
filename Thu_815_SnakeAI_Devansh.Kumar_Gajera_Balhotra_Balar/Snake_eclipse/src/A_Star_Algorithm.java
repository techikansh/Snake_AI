
import java.util.ArrayList;

import static java.lang.Math.abs;

class A_Star_Algorithm {

    int snake_x, snake_y;
    int number_of_apple;
    int apple_x_coordinate;
    int apple_y_coordinate;
    ArrayList<Snakebody> snakes;
    ArrayList<Snakebody> snakes2;
    int dir_x,dir_y;

    int[] a1 = {25, -25, 0, 0};
    int[] a2 = {0, 0, 25, -25};

    ArrayList<neighbors2> open = new ArrayList<neighbors2>();
    ArrayList<neighbors2> close = new ArrayList<neighbors2>();
//    ArrayList<neighbors2> final_list = new ArrayList<neighbors2>();

    public A_Star_Algorithm(ArrayList snakes, ArrayList snakes2, int apple_x_coordinate, int apple_y_coordinate, int snake_x, int snake_y, int number_of_apple) {
        this.snakes = snakes;
        this.snakes2 = snakes2;
        this.apple_x_coordinate = apple_x_coordinate;
        this.apple_y_coordinate = apple_y_coordinate;
        this.snake_x = snake_x;
        this.snake_y = snake_y;
        this.number_of_apple = number_of_apple;



    }



    public char find_neighbours2() {
        neighbors2 start = new neighbors2(snake_x, snake_y,Distance(snake_x, apple_x_coordinate, snake_y, apple_y_coordinate),snake_x,snake_y);
        if(next_neighbor_is_apple(start)){
            dir_x = apple_x_coordinate;
            dir_y = apple_y_coordinate;
            //System.out.println(dir_x);
            //System.out.println(dir_y);
            return direction_call(dir_x,dir_y);
        }else{
            // for(int i = 0 ; i< 4; i++){
            //     neighbors na = new neighbors(start.getNeighbors_x()+ a1[i], start.getNeighbors_y()+a2[i] );
            //     if(na.getNeighbors_x() == apple_x_coordinate && na.getNeighbors_y() == apple_y_coordinate){
            //
            //     }
            // }
            //start.distance = Distance(snake_x, apple_x_coordinate, snake_y, apple_y_coordinate);
            //start.setParent_x(snake_x);
            //start.setParent_y(snake_y);
            //start.parent_x = snake_x;
            //start.parent_y = snake_y;
            open.add(start);

            boolean flag = true;
            while (flag) {

                int max = 1000;
                int current_i = 0;
                for (int i = 0; i < open.size(); i++) {
                    if (open.get(i).getDistance() < max) {
                        max = open.get(i).getDistance();
                        current_i = i;
                    }

                }

                //System.out.println(current_i);

                neighbors2 current = new neighbors2(open.get(current_i).getNeighbors_x(), open.get(current_i).getNeighbors_y(),Distance(open.get(current_i).getNeighbors_x(),apple_x_coordinate,open.get(current_i).getNeighbors_y(),apple_y_coordinate),open.get(current_i).getParent_x(),open.get(current_i).getParent_y());
                //System.out.println(open.get
                // (current_i).getParent_x());
                //System.out.println("current_x"+ current.getNeighbors_x());
                //System.out.println("current_y"+ current.getNeighbors_y());

                for (int i = 0; i < 4; i++) {
                    neighbors2 n = new neighbors2(current.getNeighbors_x() + a1[i], current.getNeighbors_y() + a2[i],Distance(current.getNeighbors_x() + a1[i],apple_x_coordinate,current.getNeighbors_y() + a2[i],apple_y_coordinate),current.getNeighbors_x(),current.getNeighbors_y());
                    //System.out.println("n_x"+n.getNeighbors_x());
                    //System.out.println("n_y"+ n.getNeighbors_y());
                    if (n.getNeighbors_x() == apple_x_coordinate && n.getNeighbors_y() == apple_y_coordinate) {
                        //n.setParent_x(current.getNeighbors_x());
                        //n.setParent_y(current.getNeighbors_y());
                        ////n.parent_x = current.getNeighbors_x();
                        ////n.parent_y = current.getNeighbors_y();
                        //System.out.println(n.parent_x);
                        //System.out.println(n.parent_y);
                        //close.add(n);
                        flag = false;
                        continue;
                    }
                    if(is_not_in_close_list(n) && is_free(n) && is_neighbor(n)) {
                        //n.distance = Distance(n.getNeighbors_x(), apple_x_coordinate, n.getNeighbors_y(), apple_y_coordinate);
                        //n.setParent_x(current.getNeighbors_x());
                        //n.setParent_y(current.getNeighbors_y());
                        //n.parent_x = current.getNeighbors_x();
                        //n.parent_y = current.getNeighbors_y();
                        // System.out.println(n.parent_x);
                        // System.out.println(n.parent_y);
                        open.add(n);


                    }
                }
                if(open.size() > 0){
                    open.remove(current_i);
                }
                close.add(current);


            }
            //for(int i = 0 ; i< close.size(); i++){
            //    System.out.println("x = " + close.get(i).getNeighbors_x());
            //    System.out.println("y = "+ close.get(i).getNeighbors_y());
            //    System.out.println("Distance "+close.get(i).getDistance());
            //    System.out.println("Parent of x " +close.get(i).getParent_x());
            //    System.out.println("Parent of y "+close.get(i).getParent_y());
            //}
            for(int i = close.size()-1; i >= 0 ; i--){
                if(snake_x == close.get(i).getParent_x() && snake_y == close.get(i).getParent_y()){
                    dir_x = close.get(i).getNeighbors_x();
                    dir_y = close.get(i).getNeighbors_y();
                    break;
                }
            }
            //System.out.println(dir_x);
            //System.out.println(dir_y);
            return direction_call(dir_x,dir_y);
        }




    }

    public char direction_call(int x, int y) {
          if (x == snake_x + 25 && y == snake_y) {
              return 'R';
          } else if (x == snake_x - 25 && y == snake_y) {
              return 'L';
          } else if (x == snake_x && y == snake_y + 25) {
              return 'D';
          } else return 'U';
      }

    public int Distance(int x1, int x2, int y1, int y2) {
        return (abs(x1 - x2) + abs(y1 - y2))/25;
    }

    public boolean next_neighbor_is_apple(neighbors2 n){
        int c = 0;
        for(int i  = 0 ; i< 4 ; i++){
            neighbors2 na = new neighbors2(n.getNeighbors_x()+a1[i],n.getNeighbors_y()+a2[i]);
            if(na.getNeighbors_x() == apple_x_coordinate && na.getNeighbors_y() == apple_y_coordinate){
                c+=1;
            }
        }
        if (c != 0) return true;
        else return false;
    }
    public boolean is_not_in_close_list(neighbors2 n) {
        int b = 0;
        for (int i = 0; i < close.size(); i++) {
            //System.out.println(n.getNeighbors_x());
            //System.out.println(close.get(i).getNeighbors_x());
            //System.out.println(n.getNeighbors_y());
            //System.out.println(close.get(i).getNeighbors_y());
            if ((n.getNeighbors_x() == close.get(i).getNeighbors_x()) && (n.getNeighbors_y() == close.get(i).getNeighbors_y())) {
                b += 1;
            }
        }
        if (b != 0) return false;
        else return true;
    }

    public boolean is_free(neighbors2 n) {
        int a = 0, a2 = 0;
        for (int i = 0; i < snakes.size(); i++) {
            if (n.getNeighbors_x() == snakes.get(i).getSnake_x_coordinate() && n.getNeighbors_y() == snakes.get(i).getSnake_y_coordinate()) {
                a = a + 1;
            }
        }

        for (int i = 0; i < snakes2.size(); i++) {
            if (n.getNeighbors_x() == snakes2.get(i).getSnake_x_coordinate() && n.getNeighbors_y() == snakes2.get(i).getSnake_y_coordinate()) {
                a2 = a2 + 1;
            }
        }

        if (a == 0 && a2 == 0) return true;
        else return false;
    }


    public boolean is_neighbor(neighbors2 n) {
        if (n.getNeighbors_x() >= 0 && n.getNeighbors_y() >= 0 && n.getNeighbors_x() <= 600 && n.getNeighbors_y() <= 600) {
            return true;
        } else return false;
    }


}



class neighbors2{
    int neighbors_x;
    int neighbors_y;
    int distance;
    int total_free_neighbors = 0;
    int parent_x,parent_y;

    public neighbors2(int neighbors_x, int neighbors_y){
        this.neighbors_x = neighbors_x;
        this.neighbors_y = neighbors_y;
    }

    public neighbors2(int neighbors_x, int neighbors_y, int distance, int parent_x, int parent_y){
        this.neighbors_x = neighbors_x;
        this.neighbors_y = neighbors_y;
        this.distance = distance;
        this.parent_x = parent_x;
        this.parent_y = parent_y;
    }

    public void setParent_x(int parent_x) {
        this.parent_x = parent_x;
    }

    public void setParent_y(int parent_y){
        this.parent_y = parent_y;
    }

    public int getNeighbors_x(){return neighbors_x;}
    public int getNeighbors_y(){return neighbors_y;}
    public int getDistance(){return distance;}
    public int getTotal_free_neighbors(){return total_free_neighbors;}

    public int getParent_x(){return parent_x;}
    public int getParent_y(){return parent_y;}
}



