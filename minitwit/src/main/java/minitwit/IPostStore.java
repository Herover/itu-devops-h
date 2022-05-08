package minitwit;

import java.util.HashMap;
import java.util.List;

public interface IPostStore {
    public void addPost(int userId, String text) throws Exception;

    public List<HashMap<String, Object>> getLatestMessages(int perPage) throws Exception;

    public List<HashMap<String, Object>> getLatestUserMessages(int perPage, int userId) throws Exception;
}
