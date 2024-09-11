package org.jmj.services.caching;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TriePathCache implements PathCache {
    @Data
    public class TrieNode {
        private final Map<String, TrieNode> children = new HashMap<>();
        private boolean isEndOfPath;
        private HttpStatus status=HttpStatus.OK;
    }

    private final Map<String, TrieNode> roots = new HashMap<>();

    @Override
    public synchronized void insert(String subSystemName, String path) {
        TrieNode root = roots.computeIfAbsent(subSystemName, k -> new TrieNode());
        TrieNode currentNode = root;
        String[] segments = path.split("/");
        for (String segment : segments) {
            if (!segment.isEmpty()) {
                currentNode = currentNode.getChildren().computeIfAbsent(segment, s -> new TrieNode());
            }
        }
        currentNode.setEndOfPath(true);
    }
    @Override
    public synchronized void updateStatus(String subSystemName, String path, HttpStatus status) {
        TrieNode root = roots.get(subSystemName);
        if (root == null) {
            return;
        }
        TrieNode currentNode = root;
        String[] segments = path.split("/");
        for (String segment : segments) {
            if (!segment.isEmpty()) {
                currentNode = currentNode.getChildren().get(segment);
                if (currentNode == null) {
                    return;
                }
            }
        }
        if (currentNode.isEndOfPath()) {
            currentNode.setStatus(status);
        }
    }


    @Override
    public synchronized RequestPathAndContext search(String subSystemName, String path) {
        TrieNode root = roots.get(subSystemName);
        if (root == null) {
            return null;
        }
        //To retrieve the path stored in the db
        var actualPath=new StringBuilder();
        TrieNode currentNode = root;
        Map<String, String> context = new HashMap<>();
        String[] segments = path.split("/");
        for (String segment : segments) {
            if (!segment.isEmpty()) {
                //Checking for exaxt match
                TrieNode nextNode = currentNode.getChildren().get(segment);
                //Checking for dynamic match
                if (nextNode == null) {
                    for (Map.Entry<String, TrieNode> entry : currentNode.getChildren().entrySet()) {
                        if (entry.getKey().startsWith("{") && entry.getKey().endsWith("}")) {
                            nextNode = entry.getValue();
                            context.put(entry.getKey().substring(1, entry.getKey().length() - 1), segment);
                            actualPath.append("/").append(entry.getKey());
                            break;
                        }
                    }
                } else {
                    actualPath.append("/").append(segment);
                }
                if (nextNode == null) {
                    return null;
                }
                currentNode = nextNode;
            }
        }
        return currentNode.isEndOfPath()?new RequestPathAndContext(actualPath.toString(), context,currentNode.getStatus()):null;
    }

    @Override
    public synchronized void update(String subSystemName, String oldPath, String newPath) {
        delete(subSystemName, oldPath);
        insert(subSystemName, newPath);
    }

    @Override
    public synchronized void delete(String subSystemName, String path) {
        TrieNode root = roots.get(subSystemName);
        if (root == null) {
            return;
        }
        TrieNode currentNode = root;
        TrieNode parentNode = null;
        String[] segments = path.split("/");
        String lastSegment = null;
        for (String segment : segments) {
            if (!segment.isEmpty()) {
                parentNode = currentNode;
                currentNode = currentNode.getChildren().get(segment);
                if (currentNode == null) {
                    return;
                }
                lastSegment = segment;
            }
        }
        if (currentNode != null && currentNode.isEndOfPath()) {
            currentNode.setEndOfPath(false);
            if (parentNode != null && lastSegment != null) {
                parentNode.getChildren().remove(lastSegment);
            }
        }
    }
    @Override
    public synchronized void clear() {
        roots.clear();
    }

    @Override
    public synchronized void clear(String root) {
        roots.remove(root);
    }
}