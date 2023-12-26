package mobilefood;
public class StringCorrector {
    public String inputCorrector(String input)
    {
        String[] ret = input.split(",");
        String str = new String();
        int index = 0;
        while(ret[index].isEmpty())
        {
            index++;
        }
        str = ret[index];
        for(int i=index+1;i<ret.length;i++)
        {
            str+=",";
            if(ret[i].charAt(0)!=' ')
            {
                str+=" ";
            }
            str+=ret[i];
        }
        return str;
    }

    public String[] inputDivider(String[] array,int size)
    {
        String[] retArray = new String[size];
        int index = -1;   
        for(int i=0;i<array.length;i++)
        {
            if(!array[i].isEmpty() && array[i].charAt(0) == ' ')
            {
                retArray[index] += (","+array[i]);
            }
            else
            {
                retArray[++index] = array[i];
            }
        }
        return retArray;
    }
}