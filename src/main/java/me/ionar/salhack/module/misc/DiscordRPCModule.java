package me.ionar.salhack.module.misc;

import me.ionar.salhack.main.Wrapper;
import me.ionar.salhack.managers.DiscordManager;
import me.ionar.salhack.managers.ModuleManager;
import me.ionar.salhack.module.Module;
import me.ionar.salhack.module.Value;
import me.ionar.salhack.module.combat.AutoCrystalRewrite;
import me.ionar.salhack.util.entity.PlayerUtil;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DiscordRPCModule extends Module
{
    public final Value<Boolean> Username = new Value<Boolean>("Username", new String[] {"U"}, "Displays your username in the rich presence", true);
    public final Value<Boolean> ServerIP = new Value<Boolean>("ServerIP", new String[] {"S"}, "Displays your current playing server in the rich presence", true);
    public final Value<String> DetailsAddon = new Value<String>("DetailsAddon", new String[] {"D"}, "Displays a custom message after the previous", "Gaming");
    public final Value<Boolean> Quote = new Value<Boolean>("Quote", new String[] {"U"}, "Displays a quick Yuri Quote ;)", true);
    public final Value<Boolean> Speed = new Value<Boolean>("Speed", new String[] {"U"}, "Displays your speed in the rich presence", true);
    public final Value<Boolean> Movement = new Value<Boolean>("Movement", new String[] {"U"}, "Displays if you're flying/onground in the rich presence", true);
    public final Value<Boolean> Crystalling = new Value<Boolean>("Crystalling", new String[] {"U"}, "Displays the current target from autocrystal", true);
    public final Value<Boolean> Health = new Value<Boolean>("Health", new String[] {"U"}, "Displays your Health in the rich presence", true);
    public final Value<Boolean> GitHub = new Value<Boolean>("GitHub", new String[] {"U"}, "Displays the github link", false);


    public DiscordRPCModule()
    {
        super("DiscordRPC", new String[] {"RPC"}, "Shows discord rich presence for this mod", "NONE", -1, ModuleType.MISC);
        setEnabled(true);
    }

    private AutoCrystalRewrite _autoCrystal = null;

    @Override
    public void init()
    {
        _autoCrystal = (AutoCrystalRewrite)ModuleManager.Get().GetMod(AutoCrystalRewrite.class);

        if (isEnabled())
            DiscordManager.Get().enable();
    }

    @Override
    public void onEnable()
    {
        super.onEnable();

        DiscordManager.Get().enable();
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        try
        {
            DiscordManager.Get().disable();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public String generateDetails()
    {
        String result = DetailsAddon.getValue();

        if (result == null)
            result = "";

        if (ServerIP.getValue())
            result = (Wrapper.GetMC().getCurrentServerData() != null ? Wrapper.GetMC().getCurrentServerData().serverIP : "none") + " | " + result;

        if (Username.getValue())
            result = Wrapper.GetMC().session.getUsername() + " | " + result;

        return result;
    }
    public String generateState()
    {
        if (mc.player == null)
            return "Loading...";

        if (Quote.getValue())
        {
          return "Yuri is pure Gold!";
        }

        if (GitHub.getValue())
        {
            return "Nothing is better than the meaning of yuri";
        }


        String result = "";

        if (Crystalling.getValue() && _autoCrystal.isEnabled() && _autoCrystal.getTarget() != null)
            return "Crystalling " + _autoCrystal.getTarget() + " with YuriHack's autocrystal!";

        if (Movement.getValue())
        {
            result = mc.player.onGround ? "On the ground" : "Airborne";

            if (mc.player.isElytraFlying())
                result = "Zooming";
        }

        if (Speed.getValue())
        {
            float speed = PlayerUtil.getSpeedInKM();

            if (result.isEmpty())
                result = "Moving " + speed + " km/h";
            else
            {
                if (result.equals("Zooming"))
                    result += " at " + speed + " km/h";
                else
                    result += " going " + speed + " km/h";
            }
        }

        if (Health.getValue())
        {
            if (!result.isEmpty())
                result += " ";

            result += Math.floor(mc.player.getHealth() + mc.player.getAbsorptionAmount()) + " health";
        }

        return result;
    }

}
