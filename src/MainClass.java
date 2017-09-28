import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//TODO: мне нужна какая-то структура данных, которая бы хранила вершину и расстояния до других вершин
//TODO: Теперь нужно найти минимальное ребро и запомнить 2 вершины

public class MainClass {
    public static int n;
    public static ArrayList<Vertex> vertices;
    public static ArrayList<Edge> allEdges = new ArrayList<>();
    public static ArrayList<Edge> sortedEdges = new ArrayList<>();
    public static ArrayList<Edge> kruskalEdges = new ArrayList<>();

    public static void main(String[] args) {
        String rawInput = getData();
        vertices = makeGraph(rawInput);
        addEdges();
//        for (int i=0; i<n; i++)
//        {
//            System.out.print(vertices.get(i).getPoint()+" ");
//            System.out.print(vertices.get(i).getName()+" ");
//            System.out.println(vertices.get(i).getEdges());
//        }
        sortEdges();
//        for (int i = 0; i < sortedEdges.size(); i++) {
//            System.out.println(sortedEdges.get(i).getWeight());
//        }
//        System.out.println(getEither(sortedEdges.get(0)).getPoint());
        boruvkaKruskal();
        double weight = overallWeight();
        writeAnswer(weight);

    }

    public static void writeAnswer(double weight)
    {
        try{
            PrintWriter writer = new PrintWriter("out.txt", "UTF-8");
            for (int i = 0; i < n; i++) {
                writer.println(getOutputStrings().get(i)+0);
            }
            writer.println(weight);
            writer.close();
        } catch (IOException e) {

        }
    }

    public static ArrayList<String> getOutputStrings()
    {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < getAdjacent(vertices.get(i).getName()).size(); j++) {
                stringBuilder.append(getAdjacent(vertices.get(i).getName()).get(j)+" ");
            }
            list.add(stringBuilder.toString());
        }
        return list;
    }

    public static ArrayList<Integer> getAdjacent(int vertex)
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < kruskalEdges.size(); i++) {
            if (kruskalEdges.get(i).getEither() == vertex)
                list.add(kruskalEdges.get(i).getOther());
            if (kruskalEdges.get(i).getOther() == vertex)
                list.add(kruskalEdges.get(i).getEither());
        }
        Collections.sort(list);
        return list;
    }

    public static double overallWeight()
    {
        double sum = 0;
        for (int i = 0; i < kruskalEdges.size(); i++) {
            sum+=kruskalEdges.get(i).getWeight();
        }
        return sum;
    }

    public static void boruvkaKruskal()
    {
        ArrayList<ArrayList<Vertex>> forrest = createAForrest();
        int numberOfTrees = n;
        while (numberOfTrees > 1)
        {
//            System.out.println("A");
            for (int i = 0; i < sortedEdges.size(); i++) {
                if (connectsDifferentComponents(forrest, sortedEdges.get(i)))
                    //взяли мин ребро и смотрим, соединяет ли оно разные компаненты связности
                {
                    //если соединяет, добавляем его в ответ и соединяем компаненты - то бишь надо
                    //объеденить множества, в которые включены его концы
                    kruskalEdges.add(sortedEdges.get(i));
                    merge(forrest, sortedEdges.get(i));
                    numberOfTrees--;
                }
            }
        }
    }

    public static void merge(ArrayList<ArrayList<Vertex>> forrest, Edge edge)
    {
        Vertex a = getEither(edge);
        Vertex b = getOther(edge);
        ArrayList<Vertex> aSet = new ArrayList<>();
        ArrayList<Vertex> bSet = new ArrayList<>();
//        int idx1 = 0;
//        int idx2 = 0;
        for (int i = 0; i < forrest.size(); i++) {
            if (forrest.get(i).contains(a))
            {
                aSet = forrest.get(i);
//                idx1 = i;
                forrest.remove(forrest.get(i));//это точно надо тут делать?!
            }
//            if (forrest.get(i).contains(b))
//            {
//                bSet = forrest.get(i);
//                idx2 = i;
////                forrest.remove(forrest.get(i));
//            }
        }
        for (int i = 0; i < forrest.size(); i++) {
            if (forrest.get(i).contains(b))
            {
                bSet = forrest.get(i);
                forrest.remove(forrest.get(i));
            }
        }
//        System.out.println(idx1+" - "+idx2);
//        System.out.println("forrest.size:"+forrest.size());
//        forrest.remove(idx1);
//        forrest.remove(idx2);
        ArrayList<Vertex> merged = new ArrayList<>(aSet);
        merged.addAll(bSet);
        forrest.add(merged);

    }

    public static boolean connectsDifferentComponents(ArrayList<ArrayList<Vertex>> forrest, Edge edge)
    {
        boolean flag = true;
        Vertex a = getEither(edge);
        Vertex b = getOther(edge);
        for (int i = 0; i < forrest.size(); i++) {
            if (forrest.get(i).contains(a) && forrest.get(i).contains(b))
                flag = false;
        }
        return flag;
    }

    public static ArrayList<ArrayList<Vertex>> createAForrest()
    {
        ArrayList<ArrayList<Vertex>> forrest = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ArrayList<Vertex> tree = new ArrayList<>();
            tree.add(vertices.get(i));
            forrest.add(tree);
        }
        return forrest;
    }

    public static double calcDistance(Vertex from, Vertex to) {
//        return Math.sqrt(Math.pow(to.getPoint().get(0) - from.getPoint().get(0), 2)
//                + Math.pow(to.getPoint().get(1) - from.getPoint().get(1), 2));
        return Math.abs(from.getPoint().get(0) - to.getPoint().get(0))
                + Math.abs(from.getPoint().get(1) - to.getPoint().get(1));
    }

    public static String getData() {
        String line = null;
        File file = new File("in.txt");
        StringBuilder sb = new StringBuilder();

        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exists");
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);

        try {
            n = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static ArrayList<Vertex> makeGraph(String rawInput)
    {
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<String> stringPoints = new ArrayList<>(Arrays.asList(rawInput.split(";")));
        for (int i=0; i<n; i++)
        {
            Vertex vertex = new Vertex(i+1);
            vertex.setName(i+1);
            String[] stringArr = stringPoints.get(i).split("\\s+");
            ArrayList<Integer> list = new ArrayList<>();
            list.add(Integer.parseInt(stringArr[0]));
            list.add(Integer.parseInt(stringArr[1]));
            vertex.setPoint(list);
            vertices.add(vertex);
        }
        return vertices;
    }

    public static void addEdges()
    {
        for (int i=0; i<n; i++)
        {
            Vertex currentVertex = vertices.get(i);
            ArrayList<Double> edges = new ArrayList<>();
            for (int j=0; j<n; j++)
            {
                double dist = calcDistance(currentVertex, vertices.get(j));
                edges.add(dist);
                Edge edge = new Edge(i+1,j+1);
                edge.setWeight(dist);
                allEdges.add(edge);
            }
            currentVertex.setEdges(edges);
        }
    }

    public static Boolean isLessThanSecond(Edge first, Edge second)
    {
        if (first.getWeight() <= second.getWeight()) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

    public static void sortEdges()
    {
        for (int i=0; i<n*(n-1)/2; i++)
        {
            sortedEdges.add(findMinEdge());
//            System.out.print(i+":");
//            for (int j = 0; j < allEdges.size(); j++) {
//                System.out.print(allEdges.get(j).getWeight()+" "); //to see what edges we are setting 0
//                if ((j+1)%5==0)
//                    System.out.println();
//            }
//            System.out.println();
        }
    }

    public static Edge findMinEdge()
    {
        //0.0
        Edge currentMinEdge = new Edge(100, 100);
        currentMinEdge.setWeight(Integer.MAX_VALUE);//here is the problem
        for (int i=0; i<allEdges.size(); i++)
        {
            if (isLessThanSecond(allEdges.get(i),currentMinEdge) && allEdges.get(i).getWeight()!=0) {
                currentMinEdge = allEdges.get(i);
            }
        }
        Edge secondEdge = null;
        int secondEdgei=0;
        //here we need to delete the exact same edge so we won't find it the next round
        for (int i=0; i<allEdges.size(); i++)
        {
            if(allEdges.get(i).getWeight() == currentMinEdge.getWeight()) {
                secondEdge = allEdges.get(i);
                secondEdgei = i;
                break;
            }
        }
        Edge edge = new Edge(currentMinEdge.getEither(),currentMinEdge.getOther());
        edge.setWeight(currentMinEdge.getWeight());
        currentMinEdge.setWeight(0);
        allEdges.get(secondEdgei).setWeight(0);
        //secondEdge.setWeight(0);
        return edge;
    }

    public static Vertex getEither(Edge edge)
    {
        return vertices.get(edge.getEither()-1);
    }

    public static Vertex getOther(Edge edge)
    {
        return vertices.get(edge.getOther()-1);
    }


}
