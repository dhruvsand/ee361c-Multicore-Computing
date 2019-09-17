public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        FineGrainedRBTree fineGrainedRBTree = new FineGrainedRBTree();

//        for (int i=0;i<20;i++){
//            int random = (int)(Math.random()*10000+1);
//            fineGrainedRBTree.Add(random);
//            assert fineGrainedRBTree.Contains(random);
//            System.out.println("Added "+random);
//            //LockBasedRBTreeGUI lockBasedRBTreeGUI = new LockBasedRBTreeGUI(fineGrainedRBTree);
//
//        }

        fineGrainedRBTree.Add(1);
        fineGrainedRBTree.Add(2);
        fineGrainedRBTree.Add(0);
        fineGrainedRBTree.remove(1);
        //fineGrainedRBTree.remove(2);
        //fineGrainedRBTree.remove(0);

        for(int i=0;i<100;i++)
            fineGrainedRBTree.Add(i);
        for(int i=0;i<100;i++)
            fineGrainedRBTree.remove(i);

        System.out.println("Done");
        LockBasedRBTreeGUI lockBasedRBTreeGUI = new LockBasedRBTreeGUI(fineGrainedRBTree);

    }
}
