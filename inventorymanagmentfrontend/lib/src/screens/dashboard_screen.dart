import 'package:flutter/material.dart';

import '../app.dart';
import '../services/api_client.dart';
import '../services/session.dart';

class DashboardScreen extends StatefulWidget {
  const DashboardScreen({super.key});

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  final _api = ApiClient();

  Map<String, dynamic>? _health;
  String? _error;
  bool _loading = false;

  @override
  void initState() {
    super.initState();
    _loadHealth();
  }

  Future<void> _loadHealth() async {
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final res = await _api.health();
      setState(() {
        _health = res;
      });
    } catch (e) {
      setState(() {
        _error = 'No conecta con backend (${ApiClient.baseUrl}).';
      });
    } finally {
      setState(() {
        _loading = false;
      });
    }
  }

  Future<void> _seed() async {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Cargando ejemplos en Firebase...')),
    );
    try {
      await _api.seed();
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Listo: DB llenada con ejemplos.')),
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Error haciendo seed.')),
      );
    }
  }

  void _logout() {
    Session.instance.clear();
    Navigator.of(context).pushNamedAndRemoveUntil(Routes.login, (_) => false);
  }

  @override
  Widget build(BuildContext context) {
    final username = Session.instance.username ?? 'usuario';

    return Scaffold(
      appBar: AppBar(
        title: const Text('Dashboard'),
        actions: [
          IconButton(
            tooltip: 'Logout',
            onPressed: _logout,
            icon: const Icon(Icons.logout),
          ),
        ],
      ),
      body: SafeArea(
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            Text(
              'Hola, $username',
              style: Theme.of(context).textTheme.titleLarge,
            ),
            const SizedBox(height: 12),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text(
                      'Backend status',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 8),
                    if (_loading) const LinearProgressIndicator(),
                    if (_error != null)
                      Text(
                        _error!,
                        style: TextStyle(
                          color: Theme.of(context).colorScheme.error,
                        ),
                      )
                    else if (_health != null)
                      Text(
                        'status: ${_health!['status']}\nmessage: ${_health!['message']}',
                      )
                    else
                      const Text('Sin datos'),
                    const SizedBox(height: 8),
                    Align(
                      alignment: Alignment.centerRight,
                      child: TextButton.icon(
                        onPressed: _loadHealth,
                        icon: const Icon(Icons.refresh),
                        label: const Text('Refresh'),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 12),
            FilledButton.icon(
              onPressed: _seed,
              icon: const Icon(Icons.data_array),
              label: const Text('Llenar Firebase con ejemplos (seed)'),
            ),
            const SizedBox(height: 12),
            OutlinedButton.icon(
              onPressed: () => Navigator.of(context).pushNamed(Routes.markets),
              icon: const Icon(Icons.store),
              label: const Text('Ver mercados'),
            ),
          ],
        ),
      ),
    );
  }
}

