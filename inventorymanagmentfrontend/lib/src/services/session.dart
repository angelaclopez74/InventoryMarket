class Session {
  Session._();

  static final Session instance = Session._();

  String? token;
  String? username;

  bool get isLoggedIn => token != null && token!.isNotEmpty;

  void setAuth({required String token, required String username}) {
    this.token = token;
    this.username = username;
  }

  void clear() {
    token = null;
    username = null;
  }
}

