package com.pickpack.memberservice.entity;

import javax.persistence.*;

@Entity
public class RoundTicketLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_ticket_like_id")
    private Long id;

    private Boolean isDelete;
    private Integer wantedPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Ticket ticketTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Ticket ticketFrom;
}