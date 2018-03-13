package tools;

import java.util.*;

public class BoolSearch {
    private HashMap<String, HashMap<String, Integer>> index;
    private HashSet<String> set;

    public BoolSearch(HashMap<String, HashMap<String, Integer>> indirectIndex) {
        set = new HashSet<>();
        index = indirectIndex;
    }

    public HashSet<String> boolSearch(String searchString) {
        List<String> words = WordParser.getWordsForBoolSearch(searchString, false);
        List<String> bools = WordParser.getWordsForBoolSearch(searchString, true);

        //adds directories from first item to the hashset
        HashMap<String, Integer> prevDirectories = index.get(words.get(0));
        Iterator it = prevDirectories.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry) it.next();
            set.add(pair.getKey());
        }

        words.remove(0);

        for (String b : bools) {
            switch (b) {
            case "and":
                HashMap<String, Integer> nextMap = index.get(words.get(0));
                words.remove(0);
                filterAnd(nextMap);
                break;
            case "or":
                HashMap<String, Integer> nextMapOr = index.get(words.get(0));
                words.remove(0);
                filterOr(nextMapOr);
                break;
            case "not":
                HashMap<String, Integer> nextMapNot = index.get(words.get(0));
                words.remove(0);
                filterNot(nextMapNot);
                break;
            }
        }

        return set;
    }

    private void filterAnd(HashMap<String, Integer> nextMap) {
        if (nextMap == null)
            return;
        if (nextMap.size() > set.size()) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!nextMap.containsKey(key)) {
                    it.remove();
                }
            }
        } else {
            HashSet<String> oldSet = set;
            set = new HashSet<>();
            Iterator it = nextMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (oldSet.contains(key)) {
                    set.add(key);//test
                }
            }
        }
    }

    private void filterOr(HashMap<String, Integer> nextMap) {
        if (nextMap == null)
            return;
        HashSet<String> newSet = new HashSet<>();

        if (nextMap.size() > set.size()) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!nextMap.containsKey(key)) {
                    newSet.add(key);
                }
            }
            newSet.addAll(nextMap.keySet());
        } else {
            Iterator it = nextMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!set.contains(key)) {
                    newSet.add(key);
                }
            }
            newSet.addAll(set);
        }

        set = newSet;
    }

    private void filterNot(HashMap<String, Integer> nextMap) {
        if (nextMap == null)
            return;
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            if (nextMap.containsKey(it.next())) {
                it.remove();
            }
        }
    }
}
