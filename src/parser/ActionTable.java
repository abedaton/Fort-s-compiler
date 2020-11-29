package parser;

import java.util.OptionalInt;
import java.util.stream.IntStream;


/**
 * The ActionTable class is used to represent an ActionTable,
 * it contains columns and rows used to access elements more easily in the action table.
 * The data of the action table is contained in the data integer list
 */
public class ActionTable {
    private final String[] column = {"BEGINPROG", "PROGNAME", "ENDLINE", "ENDPROG", "(", ")", ":=", "+", "-", "*", "/", "VARNAME", "NUMBER", "IF", "THEN",
            "ELSE", "ENDIF", "=", ">", "WHILE", "DO", "ENDWHILE", "PRINT", "READ", "$"};

    private final String[] row = {"PROGRAM", "SPACE", "CODE", "INSTRUCTION", "ASSIGN", "EXPRARITH", "EXPRARITHBIS", "PROD", "PRODBIS", "ATOM",
            "IFVAR", "IFSEQ", "COND", "COMP", "WHILEVAR", "PRINTVAR", "READVAR"};

    private final Integer[][] data = new Integer[row.length][column.length];

    /**
     * The default constructor: calls initData
     */
    public ActionTable(){
        initData();
    }

    /**
     * This method is used to convert column name in an index.
     * The method iterates through the list of columns to find the right name
     * @param name the name in string of the column
     * @return the index of the column asked or -1 if not found
     */
    public int getColumnIndex(String name){
        OptionalInt index = IntStream.range(0, this.column.length).filter(i -> this.column[i].equals(name)).findFirst();
        if (index.isPresent()){
            return index.getAsInt();
        }
        return -1;
    }

    /**
     * This method is used to convert row name in an index.
     * The method iterates through the list of rows to find the right name
     * @param name the name in string of the row
     * @return the index of the row asked or -1 if not found
     */
    public int getRowIndex(String name){
        OptionalInt index = IntStream.range(0, this.row.length).filter(i -> this.row[i].equals(name)).findFirst();
        if (index.isPresent()){
            return index.getAsInt();
        }
        return -1;
    }

    /**
     *  This method will insert data into the action table at row rowName and column columnName
     * @param rowName the name of the row where we want to insert data
     * @param columnName the name of the column where we want to insert data
     * @param value the value of the data we want to insert
     */
    public void insertIntoData(String rowName, String columnName, int value){
        int rowIndex = getRowIndex(rowName);
        if (rowIndex == -1){
            return;
        }
        int colIndex = getColumnIndex(columnName);
        if (colIndex == -1){
            return;
        }
        data[rowIndex][colIndex] = value;
    }

    /**
     * This method is use to get data out of the action table
     * @param rowName the name of the row
     * @param columnName the name of the column
     * @return the data from the action table at row rowName and column columnName
     */
    public Integer getData(String rowName, String columnName){
        int rowIndex = getRowIndex(rowName);
        if (rowIndex == -1){
            return null;
        }
        int colIndex = getColumnIndex(columnName);
        if (colIndex == -1){
            return null;
        }

        return data[rowIndex][colIndex];
    }

    /**
     * This method overloads getData(String, String) by taking objects instead of strings
     * @param rowName the name of the row
     * @param columnName the name of the column
     * @return the call of getData call with rowName and columnName as strings
     */
    public Integer getData(Object rowName, Object columnName) {
        return getData(rowName.toString(), columnName.toString());
    }


    /**
     * Method used to fill the action table with rule numbers
     * if an entry does not have any number, then the value will be null
     */
    private void initData(){
        insertIntoData("PROGRAM", "ENDLINE", 1);
        insertIntoData("PROGRAM", "BEGINPROG", 1);

        insertIntoData("SPACE", "ENDLINE", 2);
        insertIntoData("SPACE", "BEGINPROG", 3);
        insertIntoData("SPACE", ":=", 3);
        insertIntoData("SPACE", "VARNAME", 3);
        insertIntoData("SPACE", "IF", 3);
        insertIntoData("SPACE", "ELSE", 3);
        insertIntoData("SPACE", "ENDIF", 3);
        insertIntoData("SPACE", "WHILE", 3);
        insertIntoData("SPACE", "ENDWHILE", 3);
        insertIntoData("SPACE", "PRINT", 3);
        insertIntoData("SPACE", "READ", 3);
        insertIntoData("SPACE", "$", 3);
        insertIntoData("SPACE", "ENDPROG", 3);


        insertIntoData("CODE", "VARNAME", 4);
        insertIntoData("CODE", "IF", 4);
        insertIntoData("CODE", "WHILE", 4);
        insertIntoData("CODE", "PRINT", 4);
        insertIntoData("CODE", "READ", 4);
        insertIntoData("CODE", "ENDPROG", 5);
        insertIntoData("CODE", "ELSE", 5);
        insertIntoData("CODE", "ENDIF", 5);
        insertIntoData("CODE", "ENDWHILE", 5);

        insertIntoData("INSTRUCTION", "VARNAME", 6);
        insertIntoData("INSTRUCTION", "IF", 7);
        insertIntoData("INSTRUCTION", "WHILE", 8);
        insertIntoData("INSTRUCTION", "PRINT", 9);
        insertIntoData("INSTRUCTION", "READ", 10);

        insertIntoData("ASSIGN", "VARNAME", 11);

        insertIntoData("EXPRARITH", "-", 12);
        insertIntoData("EXPRARITH", "VARNAME", 12);
        insertIntoData("EXPRARITH", "(", 12);
        insertIntoData("EXPRARITH", "NUMBER", 12);

        insertIntoData("EXPRARITHBIS", "+", 13);
        insertIntoData("EXPRARITHBIS", "-", 14);
        insertIntoData("EXPRARITHBIS", "ENDLINE", 15);
        insertIntoData("EXPRARITHBIS", "ENDPROG", 15);
        insertIntoData("EXPRARITHBIS", ")", 15);
        insertIntoData("EXPRARITHBIS", "VARNAME", 15);
        insertIntoData("EXPRARITHBIS", "", 15);
        insertIntoData("EXPRARITHBIS", "IF", 15);
        insertIntoData("EXPRARITHBIS", "ELSE", 15);
        insertIntoData("EXPRARITHBIS", "ENDIF", 15);
        insertIntoData("EXPRARITHBIS", "=", 15);
        insertIntoData("EXPRARITHBIS", ">", 15);
        insertIntoData("EXPRARITHBIS", "WHILE", 15);
        insertIntoData("EXPRARITHBIS", "ENDLINE", 15);
        insertIntoData("EXPRARITHBIS", "ENDWHILE", 15);
        insertIntoData("EXPRARITHBIS", "PRINT", 15);
        insertIntoData("EXPRARITHBIS", "READ", 15);


        insertIntoData("PROD", "(", 16);
        insertIntoData("PROD", "-", 16);
        insertIntoData("PROD", "VARNAME", 16);
        insertIntoData("PROD", "NUMBER", 16);

        insertIntoData("PRODBIS", "*", 17);
        insertIntoData("PRODBIS", "/", 18);
        insertIntoData("PRODBIS", "ENDLINE", 19);
        insertIntoData("PRODBIS", "ENDPROG", 19);
        insertIntoData("PRODBIS", ")", 19);
        insertIntoData("PRODBIS", "+", 19);
        insertIntoData("PRODBIS", "-", 19);
        insertIntoData("PRODBIS", "", 19);
        insertIntoData("PRODBIS", "VARNAME", 19);
        insertIntoData("PRODBIS", "IF", 19);
        insertIntoData("PRODBIS", "ELSE", 19);
        insertIntoData("PRODBIS", "ENDIF", 19);
        insertIntoData("PRODBIS", "=", 19);
        insertIntoData("PRODBIS", ">", 19);
        insertIntoData("PRODBIS", "WHILE", 19);
        insertIntoData("PRODBIS", "ENDWHILE", 19);
        insertIntoData("PRODBIS", "PRINT", 19);
        insertIntoData("PRODBIS", "READ", 19);


        insertIntoData("ATOM", "(", 23);
        insertIntoData("ATOM", "-", 20);
        insertIntoData("ATOM", "VARNAME", 21);
        insertIntoData("ATOM", "NUMBER", 22);

        insertIntoData("IFVAR", "IF", 24);

        insertIntoData("IFSEQ", "ELSE", 26);
        insertIntoData("IFSEQ", "ENDIF", 25);

        insertIntoData("COND", "(", 27);
        insertIntoData("COND", "-", 27);
        insertIntoData("COND", "VARNAME", 27);
        insertIntoData("COND", "NUMBER", 27);

        insertIntoData("COMP", "=", 28);
        insertIntoData("COMP", ">", 29);

        insertIntoData("WHILEVAR", "WHILE", 30);

        insertIntoData("PRINTVAR", "PRINT", 31);

        insertIntoData("READVAR", "READ", 32);





    }
}
