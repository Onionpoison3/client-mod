# MineCraftServer-Velocity

共有しやすいように、クライアントMODの作成とマイクラ鯖を分けています。

| フォルダ | 中身 | 入口 |
|---|---|---|
| `client-mod/` | Forge クライアントMOD「Vanilla Lobby Allow」のソース | `client-mod\build-vanilla-lobby-allow.ps1` |
| `server/` | Velocity、ロビー、バニラ、MOD鯖、起動スクリプト | `server\start.bat` |

鯖の操作手順は `server/手順書.md` です。MODをビルドすると、できあがった jar は `server/client-only-mods/` にコピーされます。
