const projects = document.querySelector(".projects-list");
const tickets = document.querySelector(".tickets-list");
const users = document.querySelector(".users-list");

if (projects) {
    document.getElementById("projects").removeAttribute("hidden");
}

if (tickets) {
    document.getElementById("tickets").removeAttribute("hidden");
}

if (users) {
    document.getElementById("users").removeAttribute("hidden");
}