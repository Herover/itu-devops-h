package minitwit;

import java.util.ArrayList;
import java.util.HashMap;

public interface IPostStore {
    public void addPost(int userId, String text) throws Exception;

    public ArrayList<HashMap<String, Object>> getLatestMessages(int perPage) throws Exception;

    public ArrayList<HashMap<String, Object>> getLatestUserMessages(int perPage, int userId) throws Exception;
}
