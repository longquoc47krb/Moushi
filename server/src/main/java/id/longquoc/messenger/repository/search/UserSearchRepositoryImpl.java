package id.longquoc.messenger.repository.search;

import id.longquoc.messenger.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSearchRepository extends ElasticsearchRepository<User, UUID>{
    Page<User> findByFullName(String fullName, Pageable pageable);

    @Override
    default void deleteById(UUID uuid, RefreshPolicy refreshPolicy) {

    }

    @Override
    default void delete(User entity, RefreshPolicy refreshPolicy) {

    }

    @Override
    default Page<User> searchSimilar(User entity, String[] fields, Pageable pageable) {
        return null;
    }

    @Override
    default <S extends User> S save(S entity, RefreshPolicy refreshPolicy) {
        return null;
    }

    @Override
    default <S extends User> Iterable<S> saveAll(Iterable<S> entities, RefreshPolicy refreshPolicy) {
        return null;
    }

    @Override
    default void deleteAllById(Iterable<? extends UUID> uuids, RefreshPolicy refreshPolicy) {

    }

    @Override
    default void deleteAll(Iterable<? extends User> entities, RefreshPolicy refreshPolicy) {

    }

    @Override
    default void deleteAll(RefreshPolicy refreshPolicy) {

    }
}
