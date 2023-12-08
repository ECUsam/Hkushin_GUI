package postfix;

import Constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ClassGetterFromFile {
    public String encoding;

    public List<String> scriptClass = new ArrayList<>();
    public ClassGetterFromFile(String encoding){
        this.encoding = encoding;
    }
    public ClassGetterFromFile() {
        this( "utf-16le");
    }

    private void popStringBuffer(StringBuilder buffer){
        int length = buffer.length();
        if(length>0){
            buffer.deleteCharAt(length-1);
        }
    }

    public void File2Class(String filePath) throws IOException {
        try (
            BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.forName(encoding))
        ){
            StringBuilder classBuffer = new StringBuilder();
            StringBuilder allContextBuffer = new StringBuilder();
            int  pointer;
            int  lookahead;
            int curly_num = 0;
            boolean name_mattered = false;

            while ( (pointer = reader.read()) != -1 ){
                char currentChar = (char) pointer;
                allContextBuffer.append(currentChar);
                //跳过空行
                if(currentChar == '\r' && classBuffer.length()==0) {
                    do{currentChar = (char) reader.read();}while (currentChar == '\r' || currentChar == '\n');
                }
                //跳过注释
                if(currentChar == '/') {
                    lookahead = (char) reader.read();
                    if (lookahead == '/') {
                        popStringBuffer(allContextBuffer);
                        do {
                            pointer = reader.read();
                            currentChar = (char) pointer;
                        }
                        while (currentChar != '\n' && currentChar != '\r');
                        int a = reader.read();
                        continue;
                    }
                    else if(lookahead == '*'){
                        popStringBuffer(allContextBuffer);
                        do{
                            if(pointer == Constants.EOZ)break;
                            pointer = reader.read();
                            currentChar = (char) pointer;
                        }
                        while (currentChar != '*' || ((char)reader.read()) != '/');
                        int a = reader.read();
                        continue;
                    }

                }
                classBuffer.append(currentChar);

                if(currentChar == '{'){
                    name_mattered = true;
                    curly_num += 1;
                }else if (currentChar == '}') curly_num-=1;
                if( name_mattered && curly_num ==0){
                    scriptClass.add(classBuffer.toString());
                    classBuffer.setLength(0);
                    name_mattered = false;
                    }
                }
            }
        }

    public void Folder2Class(String folderPath) {
        try (
            Stream<Path> paths = Files.walk(Paths.get(folderPath))
        ){
            paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".dat")).forEach(path -> {
                try {
                    File2Class(path.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println(folderPath);
        }
    }
}
