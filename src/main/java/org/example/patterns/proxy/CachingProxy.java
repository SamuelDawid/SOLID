package org.example.patterns.proxy;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** STATYCZNY PROXY z cache. */
public class CachingProxy implements UserService {

    private final UserService delegate;
    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    public CachingProxy(UserService delegate) { this.delegate = delegate; }

    @Override
    public Optional<String> findById(Long id) {
        String cached = cache.get(id);
        if (cached != null) {
            System.out.println("[CACHE] HIT id=" + id);
            return Optional.of(cached);
        }
        System.out.println("[CACHE] MISS id=" + id);
        Optional<String> result = delegate.findById(id);
        result.ifPresent(v -> cache.put(id, v));
        return result;
    }

    @Override
    public String save(String name) {
        cache.clear();
        return delegate.save(name);
    }

    @Override
    public List<String> findAll() { return delegate.findAll(); }
}
