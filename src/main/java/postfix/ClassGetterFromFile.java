package postfix;

import Constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import Constants.Constants_GUI;
import FileManager.PathManager;
import OPcode.TreeNode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ClassGetterFromFile {
    public String encoding;
    private final PathManager pathManager;
    public TreeNode fileNode;
    // 代码 行数
    public LinkedHashMap<String, Integer> scriptClass = new LinkedHashMap<>();
    public ClassGetterFromFile(PathManager pathManager){
        this.pathManager = pathManager;
        this.encoding = pathManager.encoding;
    }
    private int baseCodeLine = 1;


    private void popStringBuffer(StringBuilder buffer){
        int length = buffer.length();
        if(length>0){
            buffer.deleteCharAt(length-1);
        }
    }

    public void update(){
        scriptClass = new LinkedHashMap<>();
    }

    // 代码块内注释保留
    public void File2Class(String filePath) {
        fileNode = new TreeNode("fileNode");
        try (
                BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.forName(encoding))
        ){
            StringBuilder classBuffer = new StringBuilder();
            StringBuilder allContextBuffer = new StringBuilder();
            int  pointer;
            int  lookahead;
            int curly_num = 0;
            boolean name_mattered = false;
            int classLine = 0;
            boolean inExplain = false;
            while ( (pointer = reader.read()) != -1 ){
                char currentChar = (char) pointer;
                allContextBuffer.append(currentChar);
                //跳过空行
                if(currentChar == '\r' && classBuffer.length()==0) {
                    do{
                        if(currentChar=='\n'){
                            if(name_mattered) classLine+=1;else baseCodeLine+=1;
                        }
                        currentChar = (char) reader.read();
                    }while (currentChar == '\r' || currentChar == '\n');
                }
                //跳过注释
                if(currentChar == '/') {
                    lookahead = (char) reader.read();
                    if (lookahead == '/' && !name_mattered) {
                        popStringBuffer(allContextBuffer);
                        StringBuilder explain = new StringBuilder("//");
                        do {
                            pointer = reader.read();
                            currentChar = (char) pointer;
                            explain.append(currentChar);
                            if(pointer==-1)break;
                        }
                        while (currentChar != '\n' && currentChar != '\r');
                        baseCodeLine+=1;
                        fileNode.addChild(new TreeNode("explain", explain.toString()));
                        int a = reader.read();
                        continue;
                    }
                    else if(lookahead == '*' && !name_mattered){
                        popStringBuffer(allContextBuffer);
                        StringBuilder explain = new StringBuilder("//");
                        do{
                            if(currentChar=='\n'){
                                baseCodeLine+=1;
                            }
                            if(pointer == Constants.EOZ){
                                LogManager.addWaring(Constants_GUI.getDescription("explain_to_end_warning") +(baseCodeLine+classLine));
                                break;
                            }
                            pointer = reader.read();
                            currentChar = (char) pointer;
                            explain.append(currentChar);
                        }
                        while (currentChar != '*' || ((char)reader.read()) != '/');
                        int a = reader.read();
                        fileNode.addChild(new TreeNode("explain", explain.toString()));
                        continue;
                    }else {
                        if(lookahead == '*') inExplain = true;
                        classBuffer.append(currentChar);
                        currentChar = (char) lookahead;
                    }
                }
                if(currentChar=='*'){
                    reader.mark(5);
                    lookahead = reader.read();
                    if(lookahead=='/')inExplain=false;
                    reader.reset();
                }
                if(currentChar=='\n'){
                    if(name_mattered)classLine+=1;else baseCodeLine+=1;
                }
                classBuffer.append(currentChar);
                if(!inExplain){
                    if(currentChar == '{'){
                        name_mattered = true;
                        curly_num += 1;
                    }else if (currentChar == '}') curly_num-=1;
                }
                if( name_mattered && curly_num ==0){
                    scriptClass.put(classBuffer.toString(), baseCodeLine);
                    fileNode.addChild(new TreeNode("classCode", classBuffer.toString()));
                    baseCodeLine += classLine;
                    classLine = 0;
                    classBuffer.setLength(0);
                    name_mattered = false;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    public void File2Class(String filePath) {
//        try (
//            BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.forName(encoding))
//        ){
//            StringBuilder classBuffer = new StringBuilder();
//            StringBuilder allContextBuffer = new StringBuilder();
//            int  pointer;
//            int  lookahead;
//            int curly_num = 0;
//            boolean name_mattered = false;
//            int classLine = 0;
//            while ( (pointer = reader.read()) != -1 ){
//                char currentChar = (char) pointer;
//                allContextBuffer.append(currentChar);
//                //跳过空行
//                if(currentChar == '\r' && classBuffer.length()==0) {
//                    do{
//                        if(currentChar=='\n'){
//                            if(name_mattered) classLine+=1;else baseCodeLine+=1;
//                        }
//                        currentChar = (char) reader.read();
//                    }while (currentChar == '\r' || currentChar == '\n');
//                }
//                //跳过注释
//                if(currentChar == '/') {
//                    lookahead = (char) reader.read();
//                    if (lookahead == '/') {
//                        popStringBuffer(allContextBuffer);
//                        do {
//                            pointer = reader.read();
//                            currentChar = (char) pointer;
//                            if(pointer==-1)break;
//                        }
//                        while (currentChar != '\n' && currentChar != '\r');
//
//
//                        if(name_mattered)classLine+=1;else baseCodeLine+=1;
//
//
//                        int a = reader.read();
//                        continue;
//                    }
//                    else if(lookahead == '*'){
//                        popStringBuffer(allContextBuffer);
//                        do{
//                            if(currentChar=='\n'){
//                                if(name_mattered)classLine+=1;else baseCodeLine+=1;
//                            }
//                            if(pointer == Constants.EOZ){
//                                LogManager.addWaring(Constants_GUI.getDescription("explain_to_end_warning") +(baseCodeLine+classLine));
//                                break;
//                            }
//                            pointer = reader.read();
//                            currentChar = (char) pointer;
//                        }
//                        while (currentChar != '*' || ((char)reader.read()) != '/');
//                        int a = reader.read();
//                        continue;
//                    }else {
//                        classBuffer.append(currentChar);
//                        currentChar = (char) lookahead;
//                    }
//                }
//                if(currentChar=='\n'){
//                    if(name_mattered)classLine+=1;else baseCodeLine+=1;
//                }
//                classBuffer.append(currentChar);
//
//                if(currentChar == '{'){
//                    name_mattered = true;
//                    curly_num += 1;
//                }else if (currentChar == '}') curly_num-=1;
//                if( name_mattered && curly_num ==0){
//                    scriptClass.put(classBuffer.toString(), baseCodeLine);
//                    baseCodeLine += classLine;
//                    classLine = 0;
//                    classBuffer.setLength(0);
//                    name_mattered = false;
//                    }
//                }
//            }catch (IOException e){
//            e.printStackTrace();
//        }
//        }

    public void Folder2Class(String folderPath) {
        try (
            Stream<Path> paths = Files.walk(Paths.get(folderPath))
        ){
            paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".dat")).forEach(path -> {
                File2Class(path.toString());
            });
        } catch (IOException e) {
            System.out.println(folderPath);
        }
    }
}
