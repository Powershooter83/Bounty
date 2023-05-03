package me.prouge.bounty.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Heads {

    ZERO("""
            eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6
            Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv
            MGViZTdlNTIxNTE2OWE2OTlhY2M2Y2VmYTdiNzNmZGIx
            MDhkYjg3YmI2ZGFlMjg0OWZiZTI0NzE0YjI3In19fQ
            """),
    CREATE("""
            eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6
            Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv
            M2VkZDIwYmU5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFl
            YjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0
            """),
    BACK("""
            eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1MmUyY
            jkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3M
            zRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0
            """),
    SAVE("""
            eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJ
            lcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMwZjQ1MzdkMjE0ZDM4NjY2ZTYzM
            DRlOWM4NTFjZDZmN2U0MWEwZWI3YzI1MDQ5YzlkMjJjOGM1ZjY1NDV
            kZiJ9fX0
            """),

    CONFIRM("""
            eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh
            0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5ODBjMWQyMTE4MDlhO
            WI2NTY1MDg4ZjU2YTM4ZjJlZjQ5MTE1YzEwN
            TRmYTY2MjQ1MTIyZTllZWVkZWNjMiJ9fX0
            """),

    CANCEL("""
            eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6
            Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTljZGI5YWYzOGNm
            NDFkYWE1M2JjOGNkYTc2NjVjNTA5NjMyZDE0ZTY3OGYwZjE5ZjI2M2Y0NmU1NDFkOGEzMCJ9fX0
            """);
    public final String texture;


}
