package tools;

import db.IndirectIndexCollectionBridge;
import org.bson.Document;

import java.lang.reflect.Array;
import java.util.*;

public class BoolSearch {
    private HashSet<String> set;

    public BoolSearch() {
        set = new HashSet<>();
    }

//{
//	"_id" : "tea",
//	"values" : [
//		{
//			"no" : 3,
//			"path" : "/Users/raulpopovici/Desktop/facultate/riw/labprb/crawler/testfolder/1.txt"
//		},
//		{
//			"no" : 1,
//			"path" : "/Users/raulpopovici/Desktop/facultate/riw/labprb/crawler/testfolder/3.txt"
//		}
//	]
//}
//
    public HashSet<String> boolSearch(String searchString) {
        List<String> words = WordParser.getWordsForBoolSearch(searchString, false);
        List<String> bools = WordParser.getWordsForBoolSearch(searchString, true);

        //adds directories from first item to the hashset
        Document iIndexEntry = IndirectIndexCollectionBridge.getWord(words.get(0));
        List<Document> values = (List<Document>) iIndexEntry.get("values");
        words.remove(0);

//        for (String b : bools) {
//            switch (b) {
//            case "and":
//                HashMap<String, Float> nextMap = index.get(words.get(0));
//                words.remove(0);
//                filterAnd(nextMap);
//                break;
//            case "or":
//                HashMap<String, Float> nextMapOr = index.get(words.get(0));
//                words.remove(0);
//                filterOr(nextMapOr);
//                break;
//            case "not":
//                HashMap<String, Float> nextMapNot = index.get(words.get(0));
//                words.remove(0);
//                filterNot(nextMapNot);
//                break;
//            }
//        }

        return set;
    }

    private void filterAnd(HashMap<String, Float> nextMap) {
        if (nextMap == null){
            set = new HashSet<>(); //multime and empty = empty
            return;
        }
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

    private void filterOr(HashMap<String, Float> nextMap) {
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

    private void filterNot(HashMap<String, Float> nextMap) {
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
