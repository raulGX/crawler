package tools;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.HashSet;

public class BoolSearchTest extends TestCase {
    public void testAnd() {
        HashMap<String, HashMap<String, Integer>> indirectIndex = new HashMap<>();

        HashMap<String, Integer> map1 = new HashMap<>();
        map1.put("d1", 2);
        map1.put("d2", 2);
        map1.put("d3", 2);

        indirectIndex.put("ana", map1);

        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("d1", 2);
        map2.put("d2", 2);
        map2.put("c3", 2);

        indirectIndex.put("mere", map2);

        BoolSearch bs = new BoolSearch(indirectIndex);

        HashSet<String> rez = bs.boolSearch("ana and mere");
        assertEquals(2, rez.size());
    }

    public void testOr() {
        HashMap<String, HashMap<String, Integer>> indirectIndex = new HashMap<>();

        HashMap<String, Integer> map1 = new HashMap<>();
        map1.put("d1", 2);
        map1.put("d2", 2);
        map1.put("d3", 2);

        indirectIndex.put("ana", map1);

        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("d1", 2);
        map2.put("d2", 2);
        map2.put("c3", 2);

        indirectIndex.put("mere", map2);

        BoolSearch bs = new BoolSearch(indirectIndex);

        HashSet<String> rez = bs.boolSearch("ana or mere");
        assertEquals(4, rez.size());
    }

    public void testNot() {
        HashMap<String, HashMap<String, Integer>> indirectIndex = new HashMap<>();

        HashMap<String, Integer> map1 = new HashMap<>();
        map1.put("d1", 2);
        map1.put("d2", 2);
        map1.put("d3", 2);

        indirectIndex.put("ana", map1);

        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("d1", 2);
        map2.put("d2", 2);
        map2.put("c3", 2);

        indirectIndex.put("mere", map2);

        BoolSearch bs = new BoolSearch(indirectIndex);

        HashSet<String> rez = bs.boolSearch("ana not mere");
        assertEquals(1, rez.size());
    }

    public void testAll() {
        HashMap<String, HashMap<String, Integer>> indirectIndex = new HashMap<>();

        HashMap<String, Integer> map1 = new HashMap<>();
        map1.put("d1", 2);
        map1.put("d2", 2);
        map1.put("d3", 2);

        indirectIndex.put("ana", map1);

        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("d1", 2);
        map2.put("d2", 2);
        map2.put("c3", 2);

        indirectIndex.put("mere", map2);
        
        HashMap<String, Integer> map3 = new HashMap<>();
        map3.put("d1", 2);

        indirectIndex.put("pere", map3);

        BoolSearch bs = new BoolSearch(indirectIndex);

        HashSet<String> rez = bs.boolSearch("ana and mere not pere");
        assertEquals(1, rez.size());
        assertEquals(true, rez.contains("d2"));
    }
}