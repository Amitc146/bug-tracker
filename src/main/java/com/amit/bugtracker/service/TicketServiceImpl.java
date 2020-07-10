package com.amit.bugtracker.service;

import com.amit.bugtracker.dto.ChartData;
import com.amit.bugtracker.dao.TicketRepository;
import com.amit.bugtracker.entity.Project;
import com.amit.bugtracker.entity.Ticket;
import com.amit.bugtracker.entity.Ticket.TicketStatus;
import com.amit.bugtracker.entity.User;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket findById(Integer id) {
        Optional<Ticket> result = ticketRepository.findById(id);

        Ticket ticket;
        if (result.isPresent()) {
            ticket = result.get();
        } else {
            throw new RuntimeException("Did not find ticket id - " + id);
        }

        return ticket;
    }

    @Override
    public List<Ticket> findAllByProjectAndStatus(Project project, String status) {
        List<Ticket> tickets;

        if (status.toLowerCase().equals("open")) {
            tickets = ticketRepository.findAllByProjectAndStatus(project, TicketStatus.OPEN);
        } else if (status.toLowerCase().equals("closed")) {
            tickets = ticketRepository.findAllByProjectAndStatus(project, TicketStatus.CLOSED);
        } else {
            return null;
        }

        sortByPriority(tickets);
        return tickets;
    }

    @Override
    public List<Ticket> findAllByStatus(String status) {
        List<Ticket> tickets;

        if (status.toLowerCase().equals("open")) {
            tickets = ticketRepository.findAllByStatus(TicketStatus.OPEN);
        } else if (status.toLowerCase().equals("closed")) {
            tickets = ticketRepository.findAllByStatus(TicketStatus.CLOSED);
        } else {
            return null;
        }

        sortByPriority(tickets);
        return tickets;
    }

    @Override
    public List<Ticket> findAllByUserAndStatus(User user, String status) {
        List<Ticket> tickets;

        if (status.toLowerCase().equals("open")) {
            tickets = ticketRepository.findAllByProjectUsersContainingAndStatus(user, TicketStatus.OPEN);
        } else if (status.toLowerCase().equals("closed")) {
            tickets = ticketRepository.findAllByProjectUsersContainingAndStatus(user, TicketStatus.CLOSED);
        } else {
            return null;
        }

        sortByPriority(tickets);
        return tickets;
    }

    @Override
    public List<Ticket> findAllByUserAndName(User user, String name) {
        if (name == null || name.isEmpty() || name.trim().isEmpty()) {
            return null;
        }

        List<Ticket> tickets;

        if (user.isAdmin() || user.isManager()) {
            tickets = ticketRepository.findAllByTitleIsContaining(name);
        } else {
            tickets = ticketRepository.findAllByProjectUsersContainingAndTitleIsContaining(user, name);
        }
        sortByPriority(tickets);

        return tickets;
    }

    @Override
    public void save(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    public void deleteById(Integer id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public List<ChartData> getPrioritiesChartData() {
        return ticketRepository.getPrioritiesChartData();
    }

    @Override
    public List<ChartData> getProjectsChartData() {
        return ticketRepository.getProjectsChartData();
    }

    @Override
    public List<ChartData> getStatusChartData() {
        return ticketRepository.getStatusChartData();
    }

    private void sortByPriority(List<Ticket> tickets) {
        tickets.sort(Comparator.comparing(Ticket::getPriority).reversed());
    }

}
