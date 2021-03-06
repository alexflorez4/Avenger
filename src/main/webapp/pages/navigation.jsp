<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Navigation-->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
    <a class="navbar-brand" href="<%=request.getContextPath()%>">AVENGER</a>
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
            data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false"
            aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav navbar-sidenav" id="exampleAccordion">

            <%--inventory--%>
            <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Menu Levels">
                <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseMulti"
                   data-parent="#exampleAccordion">
                    <i class="fa fa-fw fa-sitemap"></i>
                    <span class="nav-link-text"><sec:authentication property='principal.username'/></span>
                </a>
                <ul class="sidenav-second-level collapse" id="collapseMulti">
                    <li>
                        <a href="<%=request.getContextPath()%>/pages/inventoryUser.jsp">Inventory</a>
                    </li>
                    <li>
                        <a href="#">Reports</a>
                    </li>
                </ul>
            </li>
            <%--end inventory--%>

            <%--orders--%>
            <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Menu Levels">
                <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseOrderMulti"
                   data-parent="#exampleAccordion">
                    <i class="fa fa-fw fa-sitemap"></i>
                    <span class="nav-link-text">Orders</span>
                </a>
                <ul class="sidenav-second-level collapse" id="collapseOrderMulti">
                    <li>
                        <a href="<%=request.getContextPath()%>/orders.do">Single Order</a>
                    </li>
                    <li>
                        <a href="<%=request.getContextPath()%>/express.do">Express Upload</a>
                    </li>
                    <li>
                        <a href="<%=request.getContextPath()%>/myPendingOrders.do">Pending</a>
                    </li>
                    <li>
                        <a href="<%=request.getContextPath()%>/myCompletedOrders.do">Completed</a>
                    </li>
                </ul>
            </li>
            <%--end orders--%>

            <%--admin--%>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
            <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Menu Levels">
                <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseAdminMulti"
                   data-parent="#exampleAccordion">
                    <i class="fa fa-fw fa-sitemap"></i>
                    <span class="nav-link-text">Admin</span>
                </a>
                <ul class="sidenav-second-level collapse" id="collapseAdminMulti">
                    <li>
                        <a href="<%=request.getContextPath()%>/pages/inventoryUpdate.jsp">Inventory</a>
                    </li>
                    <li>
                        <a class="nav-link-collapse collapsed" data-toggle="collapse" href="#collapseMultiOrders">Orders</a>
                        <ul class="sidenav-third-level collapse" id="collapseMultiOrders">
                            <li>
                                <a href="<%=request.getContextPath()%>/allNewOrdersAdmin.do">New</a>
                            </li>
                            <li>
                                <a href="<%=request.getContextPath()%>/stagedOrdersAdmin.do">Staged</a>
                            </li>
                            <li>
                                <a href="<%=request.getContextPath()%>/completedOrdersAdmin.do">Completed</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="<%=request.getContextPath()%>/pages/invoices.jsp">Invoices</a>
                    </li>
                </ul>
            </li>
            </sec:authorize>
            <%--end admin--%>

            <%--extras--%>
            <sec:authorize access="hasRole('ROLE_EXTRA')">
                <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Menu Levels">
                    <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseExtraMulti"
                       data-parent="#exampleAccordion">
                        <i class="fa fa-fw fa-sitemap"></i>
                        <span class="nav-link-text">Extras</span>
                    </a>
                    <ul class="sidenav-second-level collapse" id="collapseExtraMulti">
                        <li>
                            <a href="<%=request.getContextPath()%>/pages/marginControl.jsp">Margin Control</a>
                        </li>
                    </ul>
                </li>
            </sec:authorize>
            <%--end extras--%>

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
                <a class="nav-link" href="<%=request.getContextPath()%>/logout">
                    <i class="fa fa-fw fa-sign-out"></i>Logout</a>
            </li>
        </ul>
    </div>
</nav>