package eprocurementapi.repository.custom;

import eprocurementapi.entities.AppovalOrderInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryCustom  {
    @PersistenceContext
    private EntityManager em;

    public List<AppovalOrderInfo> findAppovalOrderInfo(List<Integer> orderId, String approver) {
        String sql = "select oa.*, o.requester  from _order o inner join _order_approval oa on o.id = oa.order_id where o.id in :orderId and oa.approver = :approver";
        Query query = em.createNativeQuery(sql, Tuple.class);
        query.setParameter("orderId", orderId);
        query.setParameter("approver", approver);
        List<Tuple> tuples = query.getResultList();

        List<AppovalOrderInfo> result = new ArrayList<>();
        for (Tuple t : tuples) {
            result.add(new AppovalOrderInfo(
                    (Integer) t.get("order_id"),
                    (Integer) t.get("step"),
                    (String) t.get("approver"),
                    (Integer) t.get("current"),
                    (String) t.get("requester")
            ));
        }
        return result;
    }
}