package store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Mark;
import ru.job4j.model.Post;

import java.util.List;
import java.util.function.Function;

public class AdRepository {
    private static final Logger LOG = LoggerFactory.getLogger(AdRepository.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final AdRepository INST = new AdRepository();
    }

    public static AdRepository instOf() {
        return Lazy.INST;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            LOG.error("Exception ", e);
        } finally {
            session.close();
        }
        return null;
    }

    public List<Post> getLastDayPosts() {
        return this.tx(session -> session.createQuery(
                "select distinct p from Post p " +
                        "join fetch p.mark " +
                        "join fetch p.body " +
                        "join fetch p.user " +
                        "left join fetch p.photos " +
                        "where date_trunc('day', p.created) = current_date ",
                Post.class)
                .list());
    }

    public List<Post> getPostsWithPhoto() {
        return this.tx(session -> session.createQuery(
                "select distinct p from Post p " +
                        "join fetch p.mark " +
                        "join fetch p.body " +
                        "join fetch p.user " +
                        "join fetch p.photos",
                Post.class)
                .list());
    }

    public List<Post> getPostsWithSpecificMark(Mark mark) {
        return this.tx(session -> session.createQuery(
                "select distinct p from Post p " +
                        "join fetch p.mark " +
                        "join fetch p.body " +
                        "join fetch p.user " +
                        "left join fetch p.photos " +
                        "where p.mark = :mark",
                Post.class)
                .setParameter("mark", mark)
                .list());
    }
}
