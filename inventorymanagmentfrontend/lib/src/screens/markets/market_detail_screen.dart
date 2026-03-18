import 'package:flutter/material.dart';

import '../../models/product.dart';
import '../../services/api_client.dart';

class MarketDetailScreen extends StatefulWidget {
  const MarketDetailScreen({super.key, required this.marketId});
  final String marketId;

  @override
  State<MarketDetailScreen> createState() => _MarketDetailScreenState();
}

class _MarketDetailScreenState extends State<MarketDetailScreen> {
  final _api = ApiClient();

  bool _loading = false;
  String? _error;
  List<Product> _products = const [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final res = await _api.listProductsByMarket(widget.marketId);
      setState(() {
        _products = res;
      });
    } catch (e) {
      setState(() {
        _error = 'No se pudieron cargar los productos.';
      });
    } finally {
      setState(() {
        _loading = false;
      });
    }
  }

  Future<void> _createProduct() async {
    final created = await showDialog<Product>(
      context: context,
      builder: (_) => const _CreateProductDialog(),
    );
    if (created == null) return;

    try {
      await _api.createProduct(marketId: widget.marketId, product: created);
      if (!mounted) return;
      await _load();
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Error creando producto.')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Productos · ${widget.marketId}'),
        actions: [
          IconButton(
            onPressed: _load,
            icon: const Icon(Icons.refresh),
            tooltip: 'Refresh',
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _createProduct,
        child: const Icon(Icons.add),
      ),
      body: SafeArea(
        child: _loading
            ? const Center(child: CircularProgressIndicator())
            : _error != null
                ? Center(child: Text(_error!))
                : _products.isEmpty
                    ? const Center(child: Text('Sin productos'))
                    : ListView.separated(
                        padding: const EdgeInsets.all(12),
                        itemCount: _products.length,
                        separatorBuilder: (context, index) =>
                            const SizedBox(height: 8),
                        itemBuilder: (context, index) {
                          final p = _products[index];
                          return Card(
                            child: ListTile(
                              title: Text(p.name),
                              subtitle: Text(
                                '${p.descripcion}\n\$${p.fixedPrice.toStringAsFixed(0)} · QR: ${p.qr}',
                              ),
                              isThreeLine: true,
                            ),
                          );
                        },
                      ),
      ),
    );
  }
}

class _CreateProductDialog extends StatefulWidget {
  const _CreateProductDialog();

  @override
  State<_CreateProductDialog> createState() => _CreateProductDialogState();
}

class _CreateProductDialogState extends State<_CreateProductDialog> {
  final _formKey = GlobalKey<FormState>();
  final _nameCtrl = TextEditingController();
  final _descCtrl = TextEditingController();
  final _priceCtrl = TextEditingController();

  @override
  void dispose() {
    _nameCtrl.dispose();
    _descCtrl.dispose();
    _priceCtrl.dispose();
    super.dispose();
  }

  void _submit() {
    if (!_formKey.currentState!.validate()) return;
    final price = double.tryParse(_priceCtrl.text.replaceAll(',', '.')) ?? 0;
    Navigator.of(context).pop(
      Product(
        id: '',
        marketId: '',
        name: _nameCtrl.text.trim(),
        descripcion: _descCtrl.text.trim(),
        fixedPrice: price,
        qr: '',
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Nuevo producto'),
      content: Form(
        key: _formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextFormField(
              controller: _nameCtrl,
              decoration: const InputDecoration(labelText: 'Nombre'),
              validator: (v) =>
                  (v == null || v.trim().isEmpty) ? 'Requerido' : null,
            ),
            const SizedBox(height: 8),
            TextFormField(
              controller: _descCtrl,
              decoration: const InputDecoration(labelText: 'Descripción'),
              validator: (v) =>
                  (v == null || v.trim().isEmpty) ? 'Requerido' : null,
            ),
            const SizedBox(height: 8),
            TextFormField(
              controller: _priceCtrl,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(labelText: 'Precio fijo'),
              validator: (v) {
                if (v == null || v.trim().isEmpty) return 'Requerido';
                final p = double.tryParse(v.replaceAll(',', '.'));
                if (p == null || p <= 0) return 'Debe ser > 0';
                return null;
              },
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Cancelar'),
        ),
        FilledButton(
          onPressed: _submit,
          child: const Text('Crear'),
        ),
      ],
    );
  }
}

