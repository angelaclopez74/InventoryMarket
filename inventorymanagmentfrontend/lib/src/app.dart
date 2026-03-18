import 'package:flutter/material.dart';

import 'screens/dashboard_screen.dart';
import 'screens/login_screen.dart';
import 'screens/markets/market_detail_screen.dart';
import 'screens/markets/markets_screen.dart';
import 'services/session.dart';

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = ThemeData(
      colorSchemeSeed: const Color(0xFF1F7A8C),
      useMaterial3: true,
      inputDecorationTheme: const InputDecorationTheme(
        border: OutlineInputBorder(),
      ),
    );

    return MaterialApp(
      title: 'Inventory Management',
      theme: theme,
      initialRoute: Session.instance.isLoggedIn ? Routes.dashboard : Routes.login,
      onGenerateRoute: (settings) {
        switch (settings.name) {
          case Routes.login:
            return MaterialPageRoute(builder: (_) => const LoginScreen());
          case Routes.dashboard:
            return MaterialPageRoute(builder: (_) => const DashboardScreen());
          case Routes.markets:
            return MaterialPageRoute(builder: (_) => const MarketsScreen());
          case Routes.marketDetail:
            final marketId = settings.arguments as String?;
            if (marketId == null || marketId.isEmpty) {
              return MaterialPageRoute(
                builder: (_) => const _RouteErrorScreen(
                  message: 'Falta marketId para abrir el mercado.',
                ),
              );
            }
            return MaterialPageRoute(
              builder: (_) => MarketDetailScreen(marketId: marketId),
            );
          default:
            return MaterialPageRoute(
              builder: (_) => const _RouteErrorScreen(
                message: 'Ruta no encontrada.',
              ),
            );
        }
      },
    );
  }
}

abstract class Routes {
  static const login = '/login';
  static const dashboard = '/';
  static const markets = '/markets';
  static const marketDetail = '/markets/detail';
}

class _RouteErrorScreen extends StatelessWidget {
  const _RouteErrorScreen({required this.message});
  final String message;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Error')),
      body: Center(child: Text(message)),
    );
  }
}

