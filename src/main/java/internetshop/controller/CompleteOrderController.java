package internetshop.controller;

import internetshop.lib.Injector;
import internetshop.model.ShoppingCart;
import internetshop.service.OrderService;
import internetshop.service.ShoppingCartService;
import internetshop.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CompleteOrderController extends HttpServlet {
    private static final String USER_ID = "user_id";
    private static final Injector INJECTOR = Injector.getInstance("internetshop");
    private final ShoppingCartService shoppingCartService =
            (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);
    private final OrderService orderService =
            (OrderService) INJECTOR.getInstance(OrderService.class);
    private final UserService userService =
            (UserService) INJECTOR.getInstance(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute(USER_ID);
        ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);
        if (shoppingCart.getProducts().isEmpty()) {
            req.setAttribute("message",
                    "Your cart is empty. Please add products to complete order.");
            req.getRequestDispatcher("/WEB-INF/views/shoppingcart.jsp")
                    .forward(req, resp);
        } else {
            orderService.completeOrder(shoppingCart.getProducts(),
                    userService.get(userId));
            shoppingCartService.clear(shoppingCart);
            resp.sendRedirect(req.getContextPath() + "/ordercreated");
        }
    }
}
