import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.awt.geom.Point2D.Double;
public class StevenSmartAI extends Player {
    private int[] tempData;
    private int nMovesToStore = 8;
    private int nTimesToLookAhead = 3;
    private int lookAheadCounter =0;

    public StevenSmartAI(int color, String name) {
        super(color, name);
    }

    @Override
    public Move getMove(BlokusBoard board) {
        //System.out.println("my color is "+getColor() + " the turn is "+board.getTurn());
        ArrayList<IntPoint> avaiableMoves = board.moveLocations(getColor());
        Collections.shuffle(avaiableMoves);

        //System.out.println("available move locations "+avaiableMoves);
        ArrayList<Integer> usableShapePositions = new ArrayList<>();
        boolean[] used = (getColor()==BlokusBoard.ORANGE)?board.getOrangeUsedShapes():board.getPurpleUsedShapes();
        for(int x=0; x<used.length; x++)
            if(!used[x])
                usableShapePositions.add(x);
        //System.out.println("usable pieces "+ Arrays.toString(used));
    //    Collections.shuffle(usableShapePositions);
    /*    for(int a =0; a<usableShapePositions.size(); a++) {
            System.out.print(board.getShapes().get(usableShapePositions.get(a)).getScore());
        }
        System.out.println();
        System.out.println();
        for(int a =1; a<usableShapePositions.size(); a++) {
            int temp = usableShapePositions.get(a);
            int j = a;

            while(j>0&&board.getShapes().get(usableShapePositions.get(j-1)).getScore()<board.getShapes().get(temp).getScore())
            {
    //            Shape tempShape = board.getShapes().get(usableShapePositions.get(a));
     /*           usableShapePositions.remove(j);
                usableShapePositions.add(j-1,temp);
                usableShapePositions.set(j,usableShapePositions.get(j-1));
                j=j-1;
            }

            usableShapePositions.set(j,temp);
        }*/


 /*       int[] shapeSizeScores = new int[usableShapePositions.size()];



        for(int a =0; a<usableShapePositions.size(); a++)
        {
            shapeSizeScores [a] = board.getShapes().get(usableShapePositions.get(a)).numberOfBlocksInShape();
        }


        for(int a =0; a<usableShapePositions.size(); a++) {
            System.out.print(board.getShapes().get(usableShapePositions.get(a)).getScore());
        }*/

        if(usableShapePositions.isEmpty() ||avaiableMoves.isEmpty())
            return null;
        else
        {
            //System.out.println("heblo");

            ArrayList<Move> allMoves = new ArrayList<Move>();

            Move move = null;
            for(IntPoint movLoc: avaiableMoves)
                for(Integer position: usableShapePositions)
                {
                    for(int i=0; i<8;i++) {
                        boolean flip = i > 3;
                        int rotation = i % 4;
                        boolean[][] shape = board.getShapes().get(position).manipulatedShape(flip, rotation);
                        for (int r = -shape.length+1; r <shape.length;  r++)
                            for (int c = -shape[0].length+1; c < shape[0].length; c++)
                            {
                                IntPoint topLeft = new IntPoint(movLoc.getX()+c,movLoc.getY()+r);
                                Move test = new Move(position,flip,rotation,topLeft);

             /*                   double score = gradeMove(board,test);

                                if(board.isValidMove(test,getColor())&&score>=0.5) {
                                    System.out.println(score);
                                    return test;
                                }*/

                                allMoves.add(test);
                            }
                    }
                }

    //            System.out.print(allMoves.size());
                Move bestMove = chooseBestMove(board,allMoves);



     /*           ArrayList<Move> bestNMoves = chooseNBestMoves(board,allMoves);
                ArrayList<java.lang.Double> newScores = new ArrayList<java.lang.Double>();

                for(int x =0 ; x<bestNMoves.size() ; x++)
                {
                    double scoreOfMove = gradeMove(board,bestNMoves.get(x));
                    board.makeMove(bestNMoves.get(x),getColor());
                    Move opponentsBestMove = getMove(board);
                    double scoreOfOpponentsBestMove = gradeMove(board,opponentsBestMove);
                    double currentScoreOfPiece = scoreOfMove - scoreOfOpponentsBestMove/2;
                    board.undoMovePiece(bestNMoves.get(x),getColor());
                    newScores.add(currentScoreOfPiece);
                }
*/
            return bestMove;
        }
    }


    public ArrayList<Move> chooseNBestMoves(BlokusBoard board, ArrayList<Move> moves)
    {
        ArrayList<Point2D> scoresForEachIndex = new ArrayList<Point2D>();
        double bestScore = -100;

        int indexOfBestScore =-1;
        double o =0.0;

        for(int a = 0; a<moves.size() ; a++)
        {
            scoresForEachIndex.add(new Point2D.Double(o,gradeMove(board,moves.get(a))));
            if(gradeMove(board,moves.get(a))>bestScore) {
                bestScore = gradeMove(board, moves.get(a));
                indexOfBestScore = a;
            }
            o+= 1.0;
        }
        if(indexOfBestScore == -1)
            return null;
        for(int a =1; a<scoresForEachIndex.size(); a++) {
            Point2D temp = scoresForEachIndex.get(a);
            int j = a;

            while (j > 0 && scoresForEachIndex.get(j - 1).getY() < temp.getY()) {
                //            Shape tempShape = board.getShapes().get(usableShapePositions.get(a));
     /*           usableShapePositions.remove(j);
                usableShapePositions.add(j-1,temp);*/
                scoresForEachIndex.set(j, scoresForEachIndex.get(j - 1));
                j = j - 1;
            }

            scoresForEachIndex.set(j,temp);
        }
        ArrayList<Move> bestNMoves =new ArrayList<Move>();

        for(int aa= 0; aa<nMovesToStore ; aa++) {
            bestNMoves.add(moves.get((int)scoresForEachIndex.get(aa).getX()));
        }
  /*          for(int aa= 0; aa<nMovesToStore ; aa++) {
                System.out.print(bestNMoves.get(aa).getPieceNumber()+" ");
            }
   //
            System.out.println();*/






        return bestNMoves;

    }
    public Move chooseBestMove(BlokusBoard board, ArrayList<Move> moves)
    {

        double bestScore = -1000;

        int indexOfBestScore =-1;
    /*    double o =0.0;

        for(int a = 0; a<moves.size() ; a++)
        {
            scoresForEachIndex.add(new Point2D.Double(o,gradeMove(board,moves.get(a))));
            if(gradeMove(board,moves.get(a))>bestScore) {
                bestScore = gradeMove(board, moves.get(a));
                indexOfBestScore = a;
            }
            o+= 1.0;
        }

        for(int a =1; a<scoresForEachIndex.size(); a++) {
            Point2D temp = scoresForEachIndex.get(a);
            int j = a;

            while (j > 0 && scoresForEachIndex.get(j - 1).getY() < temp.getY()) {
                //            Shape tempShape = board.getShapes().get(usableShapePositions.get(a));
     /*           usableShapePositions.remove(j);
                usableShapePositions.add(j-1,temp);
                scoresForEachIndex.set(j, scoresForEachIndex.get(j - 1));
                j = j - 1;
            }

            scoresForEachIndex.set(j,temp);
        }*/
        for(int a = 0; a<moves.size() ; a++)
        {

            if(gradeMove(board,moves.get(a))>bestScore) {
                bestScore = gradeMove(board, moves.get(a));
                indexOfBestScore = a;
            }

        }
     /*       ArrayList<Move> bestNMoves =new ArrayList<Move>();

            for(int aa= 0; aa<nMovesToStore ; aa++) {
                bestNMoves.add(moves.get((int)scoresForEachIndex.get(aa).getX()));
            }
            for(int aa= 0; aa<nMovesToStore ; aa++) {
                System.out.print(bestNMoves.get(aa).getPieceNumber()+" ");
            }
   //
            System.out.println();*/




        if(indexOfBestScore == -1)
            return null;

        return moves.get(indexOfBestScore);

    }
    public double gradeMove(BlokusBoard board, Move move)
    {
        if(board.isValidMove(move,getColor())) {

            board.changeTurns();

            int previousOpponetMoves = board.moveLocations(getOpponentColor(getColor())).size();

            board.changeTurns();


            board.makeMove(move, getColor());
            board.changeTurns();
            int numberOfPiecesInShape =0;

            for(int a = 0; a<board.getShapes().get(move.getPieceNumber()).original().length;a++)
            {
                for(int b =0 ; b<board.getShapes().get(move.getPieceNumber()).original()[0].length;b++)
                {
                    if(board.getShapes().get(move.getPieceNumber()).original()[a][b])
                        numberOfPiecesInShape++;
                }
            }

            double c = board.moveLocations(getColor()).size() + numberOfPiecesInShape*3;
      //      System.out.print(c+" ");
            board.changeTurns();
            int cornersBlocked = previousOpponetMoves - board.moveLocations(getOpponentColor(getColor())).size();
            board.changeTurns();
            if(cornersBlocked == previousOpponetMoves)
                c += 10000000;
            c += cornersBlocked*5;
     //       System.out.println(c);
    //        System.out.println(board.moveLocations(getOpponentColor()).size());
         //   c+= 1/(Math.abs(7-move.getPoint().getX())+1)*(1/(Math.abs(7-move.getPoint().getY())+1))*500;
            c-= Math.sqrt(Math.pow(6.5-move.getPoint().getX(),2)+Math.pow(6.5-move.getPoint().getY(),2))*2;
     /*       for (int r = 0; r < board.getShapes().get(move.getPieceNumber()).shape().length; r++)
                if (board.getBoard()[r][0] == getColor())
                    c -= 10;

            for (int cc = 0; cc < board.getBoard().length; cc++)
                if (board.getBoard()[0][cc] == getColor())
                    c -= 10;*/

            board.undoMovePiece(move,getColor());
            return c;
        }
        else
            return -100000;



      //  if(move.getPoint().getX() == 0 || move.getPoint().getX() == 0||move.getPoint().getX() == 0||move.getPoint().getX() == 0)

    }

 /*   public static void insertionSort(ArrayList<Integer> data)
    {
        for(int i=1;i<data.length;i++)
        {
            int temp=data[i];
            int j=i;

            while(j>0&&data[j-1]>temp)
            {
                data[j]=data[j-1];
                j=j-1;
            }

            data[j]=temp;

        }

    }

    public void mergeSort(int[] data, int from, int to)
    {
        if(from==0 && to == data.length-1)
            tempData= new int[data.length];
        if(to == from)
            return;

        int middle = (from+to)/2;
        mergeSort(data, from,middle);
        mergeSort(dat
        int j = middle+1;a,middle+1, to);
        int k = from;
        int i = from;
        while(i <= middle && j <=to)
        {
            if(data[i] < data[j])
                tempData[k++] = data[i++];
            else
                tempData[k++] = data[j++];
        }

        while(i <= middle)
        {
            tempData[k++] = data[i++];
        }
        while(j <= to)
        {
            tempData[k++] = data[j++];
        }

        for(k = from; k<=to; k++)
            data[k] = tempData[k];
    }*/

    public int getOpponentColor(int color)
    {
        if(color == BlokusBoard.ORANGE)
            return BlokusBoard.PURPLE;
        else
            return BlokusBoard.ORANGE;
    }
    @Override
    public Player freshCopy() {
        return new StevenSmartAI(getColor(),getName());
    }
}
