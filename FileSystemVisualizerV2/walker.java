package FileSystemVisualizerV2;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class walker implements FileVisitor<Path>{
    private ArrayList<Long> visits;
    private ArrayList<String> displayValues;
    private ArrayList<File> files;
    private int visitedDirectories;
    private int visitedFiles;

    public walker(){
       visits = new ArrayList<>();
       displayValues = new ArrayList<>();
       visitedDirectories = 0;
       visitedFiles = 0;
       files = new ArrayList<>();

       files.ensureCapacity(15000);
       visits.ensureCapacity(15000);
       displayValues.ensureCapacity(15000);
    }
    
    public ArrayList<File> getFiles(){
        return new ArrayList<File>(files);
    }

    public void printVisits(){
        visits.forEach(System.out::println);
    }

    public List<Long> getList(){
        List<Long> ret  = new ArrayList<>(visits);
        return ret;
    }

    public String[] getFileStats(){
        long totalFileSize = visits.stream().reduce(Long::sum).get();
        long maxFileSize = visits.stream().max(Long::compareTo).get();
        String[] returnValues = new String[5];
        returnValues[0] = Long.valueOf(totalFileSize).toString();
        returnValues[1] = Long.valueOf(maxFileSize).toString();
        returnValues[2] = Integer.valueOf(visitedDirectories).toString();
        returnValues[3] = Integer.valueOf(visitedFiles).toString();
        return returnValues;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exec){
        visitedDirectories++;
        return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes s){
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path dir, BasicFileAttributes s){
        visits.add(s.size());
        visitedFiles++;
        files.add(dir.toFile());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path dir, IOException exec){
        return FileVisitResult.CONTINUE;
    }
}