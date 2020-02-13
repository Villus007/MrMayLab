import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
public class VMTranslator {
    public VMTranslator (String nothing){
    }
    public static void main(String[] args) throws IOException {
        File neon;
        ArrayList<File> vpn = new ArrayList<>();
        Scanner read = new Scanner(System.in);
        System.out.println("Enter file name:");
        Path line = Paths.get(read.nextLine());
        File way = line.toFile();
        if(way.exists())
        {
            String check = line.toString() + "\\" + line.getFileName().toString() + ".asm";
            System.out.println(check);
            neon = new File(check);
            neon.createNewFile();
            for(File nfile : way.listFiles(new ExtentionFilter(".vm")))
            {
                vpn.add(nfile);
            }
            CodeWriter write = new CodeWriter(neon.toString());
            for(File mfile : vpn){
                Parser parse = new Parser(mfile.getPath());
                while(parse.hasMoreCommands()){
                    parse.advance();
                    Parser.CommandType current = parse.commandType();
                    if(current != null){
                        if(current == Parser.CommandType.C_PUSH || current == Parser.CommandType.C_POP){
                            write.writePushPop(current, parse.arg1(), parse.arg2());
                        }
                        else if(current == Parser.CommandType.C_ARITHMETIC)
                        {
                            write.writeArithmetic(parse.arg1());
                        }
                        else if(current == Parser.CommandType.C_LABEL)
                        {
                            write.writeLabel(parse.arg1());
                        }
                        else if(current == Parser.CommandType.C_GOTO)
                        {
                            write.writeGoto(parse.arg1());
                        }
                        else if(current == Parser.CommandType.C_IF)
                        {
                            write.writeIf(parse.arg1());
                        }
                        else if(current == Parser.CommandType.C_FUNCTION)
                        {
                            write.writeFunction(parse.arg1(), parse.arg2());
                        }
                        else if(current == Parser.CommandType.C_RETURN)
                        {
                            write.writeReturn();
                        }
                    }
                }
            }
            write.close();
        }
        System.out.println("File created");
    }
}

class ExtentionFilter implements FilenameFilter{
    private String extension;
    public ExtentionFilter(String extension){
        this.extension = extension.toLowerCase();
    }
    @Override
    public boolean accept(File d, String name){
        return name.toLowerCase().endsWith(extension);
    }
}