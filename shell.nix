let
  pkgs = (import (builtins.fetchTarball {
    url =
      "https://nixos.org/channels/nixos-unstable/nixexprs.tar.xz";
  }) { });
in pkgs.mkShell {
  packages = [
    pkgs.sbt
    pkgs.jdk19_headless
  ];
}
