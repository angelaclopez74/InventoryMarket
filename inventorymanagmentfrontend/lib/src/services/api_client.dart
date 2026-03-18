import 'dart:convert';

import 'package:http/http.dart' as http;

import '../models/market.dart';
import '../models/product.dart';

class ApiClient {
  ApiClient({http.Client? httpClient}) : _http = httpClient ?? http.Client();

  final http.Client _http;

  // Para Android emulator suele ser 10.0.2.2 en vez de localhost.
  static const String baseUrl = 'http://localhost:8080';

  Uri _uri(String path) => Uri.parse('$baseUrl$path');

  Future<Map<String, dynamic>> health() async {
    final res = await _http.get(_uri('/api/system/health'));
    _throwIfNotOk(res);
    return jsonDecode(res.body) as Map<String, dynamic>;
  }

  Future<Map<String, dynamic>> seed() async {
    final res = await _http.post(_uri('/api/system/seed'));
    _throwIfNotOk(res);
    return jsonDecode(res.body) as Map<String, dynamic>;
  }

  Future<Map<String, dynamic>> login({
    required String username,
    required String password,
  }) async {
    final res = await _http.post(
      _uri('/api/auth/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'username': username, 'password': password}),
    );
    if (res.statusCode == 401) {
      return {'ok': false, 'message': 'Credenciales inválidas'};
    }
    _throwIfNotOk(res);
    return jsonDecode(res.body) as Map<String, dynamic>;
  }

  Future<List<Market>> listMarkets() async {
    final res = await _http.get(_uri('/api/markets'));
    _throwIfNotOk(res);
    final raw = jsonDecode(res.body) as List<dynamic>;
    return raw.map((e) => Market.fromJson(e as Map<String, dynamic>)).toList();
  }

  Future<Market> createMarket(Market market) async {
    final res = await _http.post(
      _uri('/api/markets'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(market.toJson()),
    );
    _throwIfNotOk(res);
    return Market.fromJson(jsonDecode(res.body) as Map<String, dynamic>);
  }

  Future<List<Product>> listProductsByMarket(String marketId) async {
    final res = await _http.get(_uri('/api/products/market/$marketId'));
    _throwIfNotOk(res);
    final raw = jsonDecode(res.body) as List<dynamic>;
    return raw
        .map((e) => Product.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<Product> createProduct({
    required String marketId,
    required Product product,
  }) async {
    final res = await _http.post(
      _uri('/api/products/market/$marketId'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(product.toJson()),
    );
    _throwIfNotOk(res);
    return Product.fromJson(jsonDecode(res.body) as Map<String, dynamic>);
  }

  void _throwIfNotOk(http.Response res) {
    if (res.statusCode < 200 || res.statusCode >= 300) {
      throw HttpException(res.statusCode, res.body);
    }
  }
}

class HttpException implements Exception {
  HttpException(this.statusCode, this.body);

  final int statusCode;
  final String body;

  @override
  String toString() => 'HttpException($statusCode): $body';
}

