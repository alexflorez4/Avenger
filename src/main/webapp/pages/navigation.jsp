<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Navigation-->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
    <a class="navbar-brand" href="<%=request.getContextPath()%>">AVENGER</a>
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav navbar-sidenav" id="exampleAccordion">

            <%--inventory--%>
            <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Menu Levels">
                <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseMulti" data-parent="#exampleAccordion">
                    <i class="fa fa-fw fa-sitemap"></i>
                    <span class="nav-link-text">Inventory</span>
                </a>
                <ul class="sidenav-second-level collapse" id="collapseMulti">
                    <li>
                        <a class="nav-link-collapse collapsed" data-toggle="collapse" href="#collapseMulti2">My Market Places</a>
                        <ul class="sidenav-third-level collapse" id="collapseMulti2">
                            <li>
                                <a href="#">Amazon</a>
                            </li>
                            <li>
                                <a href="#">Ebay</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a class="nav-link-collapse collapsed" data-toggle="collapse" href="#collapseMulti3">Available</a>
                        <ul class="sidenav-third-level collapse" id="collapseMulti3">
                            <li>
                                <a href="#">Electronics</a>
                            </li>
                            <li>
                                <a href="#">Mochilas</a>
                            </li>
                            <li>
                                <a href="#">Perfumes</a>
                            </li>
                            <li>
                                <a href="#">Toys</a>
                            </li>
                            <li>
                                <a href="#">Other</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a class="nav-link-collapse collapsed" data-toggle="collapse" href="#collapseMulti4">Admin</a>
                        <ul class="sidenav-third-level collapse" id="collapseMulti4">
                            <li>
                                <a href="#">Electronics</a>
                            </li>
                            <li>
                                <a href="#">Mochilas</a>
                            </li>
                            <li>
                                <a href="#">Perfumes</a>
                            </li>
                            <li>
                                <a href="<%=request.getContextPath()%>/pages/inventoryToys.jsp">Toys</a>
                            </li>
                            <li>
                                <a href="#">Other</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
            <%--end inventory--%>

        </ul>
        <ul class="navbar-nav sidenav-toggler">
            <li class="nav-item">
                <a class="nav-link text-center" id="sidenavToggler">
                    <i class="fa fa-fw fa-angle-left"></i>
                </a>
            </li>
        </ul>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" data-toggle="modal" data-target="#exampleModal">
                    <i class="fa fa-fw fa-sign-out"></i>Logout</a>
            </li>
        </ul>
    </div>
</nav>