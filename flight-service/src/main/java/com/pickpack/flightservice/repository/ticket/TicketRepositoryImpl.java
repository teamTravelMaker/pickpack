package com.pickpack.flightservice.repository.ticket;

import com.pickpack.flightservice.entity.Ticket;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.pickpack.flightservice.entity.QFlight.flight;
import static com.pickpack.flightservice.entity.QTendency.tendency;
import static com.pickpack.flightservice.entity.QTicket.ticket;

public class TicketRepositoryImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TicketRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Ticket> findAllTickets(Pageable pageable, String departure, String destination, String date, int minPrice, int maxPrice) {
        JPAQuery<Ticket> query  = queryFactory
                .selectFrom(ticket)
                .leftJoin(ticket.flightList, flight)
                .leftJoin(ticket.tendency, tendency)
                .where(ticket.depCode.eq(departure),
                        ticket.arrCode.eq(destination),
                        ticket.depDate.eq(date),
                        ticket.price.between(minPrice, maxPrice)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct();

        sortAndOrder(query, pageable);

        List<Ticket> ticketList = query.fetch();

        System.out.println("티켓사이즈: " + ticketList.size());

        Long totalCount =  queryFactory
                .select(ticket.countDistinct())
                .from(ticket)
                .leftJoin(ticket.flightList, flight)
                .leftJoin(ticket.tendency, tendency)
                .where(ticket.depCode.eq(departure),
                        ticket.arrCode.eq(destination),
                        ticket.depDate.eq(date),
                        ticket.price.between(minPrice, maxPrice)
                )
                .fetchOne();

        System.out.println("총개수: " + totalCount);

        return new PageImpl<>(ticketList, pageable, totalCount);
    }

    @Override
    public Page<Ticket> findWaypoint0or1Tickets(Pageable pageable, String departure, String destination, String date, int minPrice, int maxPrice, int waypointNum) {
        JPAQuery<Ticket> query  = queryFactory
                .selectFrom(ticket).distinct()
                .leftJoin(ticket.flightList, flight)
                .leftJoin(ticket.tendency, tendency)
                .where(ticket.depCode.eq(departure),
                        ticket.arrCode.eq(destination),
                        ticket.depDate.eq(date),
                        ticket.price.between(minPrice, maxPrice),
                        ticket.waypointNum.eq(waypointNum)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        sortAndOrder(query, pageable);

        List<Ticket> ticketList = query.fetch().stream().collect(Collectors.toList());

        System.out.println(ticketList.size());

        Long totalCount = queryFactory
                .select(ticket.countDistinct())
                .from(ticket)
                .leftJoin(ticket.flightList, flight)
                .leftJoin(ticket.tendency, tendency)
                .where(ticket.depCode.eq(departure),
                        ticket.arrCode.eq(destination),
                        ticket.depDate.eq(date),
                        ticket.price.between(minPrice, maxPrice),
                        ticket.waypointNum.eq(waypointNum)
                )
                .fetchOne();

        return new PageImpl<>(ticketList, pageable, totalCount);
    }

    @Override
    public Page<Ticket> findWaypointIsGraterThanTickets(Pageable pageable, String departure, String destination, String date, int minPrice, int maxPrice, int waypointNum) {
        JPAQuery<Ticket> query  = queryFactory
                .selectFrom(ticket).distinct()
                .leftJoin(ticket.flightList, flight)
                .leftJoin(ticket.tendency, tendency)
                .where(ticket.depCode.eq(departure),
                        ticket.arrCode.eq(destination),
                        ticket.depDate.eq(date),
                        ticket.price.between(minPrice, maxPrice),
                        ticket.waypointNum.gt(waypointNum)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        sortAndOrder(query, pageable);

        List<Ticket> ticketList = query.fetch().stream().collect(Collectors.toList());

        System.out.println(ticketList.size());

        Long totalCount = queryFactory
                .select(ticket.countDistinct())
                .from(ticket)
                .leftJoin(ticket.flightList, flight)
                .leftJoin(ticket.tendency, tendency)
                .where(ticket.depCode.eq(departure),
                        ticket.arrCode.eq(destination),
                        ticket.depDate.eq(date),
                        ticket.price.between(minPrice, maxPrice),
                        ticket.waypointNum.eq(waypointNum)
                )
                .fetchOne();

        return new PageImpl<>(ticketList, pageable, totalCount);
    }

    private void sortAndOrder(JPAQuery<Ticket> query, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            if(o.getProperty().equals("updown")) {
                PathBuilder pathBuilder = new PathBuilder(tendency.getType(), tendency.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            } else {
                PathBuilder pathBuilder = new PathBuilder(ticket.getType(), ticket.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }
    }

}
