package com.zombiemmo.gui;

import com.zombiemmo.entity.Player;
import com.zombiemmo.skill.Skill;
import com.zombiemmo.skill.SkillType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel displaying all player skills
 */
public class SkillsPanel extends JPanel {
    private Player player;
    private JPanel skillsListPanel;

    public SkillsPanel(Player player) {
        this.player = player;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Skills",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));

        // Scrollable skills list
        skillsListPanel = new JPanel();
        skillsListPanel.setLayout(new BoxLayout(skillsListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(skillsListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        updateSkills();
    }

    public void updateSkills() {
        skillsListPanel.removeAll();

        for (SkillType type : SkillType.values()) {
            Skill skill = player.getSkillManager().getSkill(type);
            JPanel skillPanel = createSkillPanel(skill);
            skillsListPanel.add(skillPanel);
            skillsListPanel.add(Box.createVerticalStrut(5));
        }

        skillsListPanel.revalidate();
        skillsListPanel.repaint();
    }

    private JPanel createSkillPanel(Skill skill) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Skill name and level
        JLabel nameLabel = new JLabel(skill.getType().getDisplayName() + " - Level " + skill.getLevel());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(nameLabel);

        // Experience bar
        long currentXP = skill.getExperience();
        long nextLevelXP = skill.getLevel() < 99 ?
            Skill.getExperienceForLevel(skill.getLevel() + 1) :
            Skill.getExperienceForLevel(99);
        long currentLevelXP = Skill.getExperienceForLevel(skill.getLevel());

        double progress = skill.getLevel() >= 99 ? 1.0 :
            (double)(currentXP - currentLevelXP) / (nextLevelXP - currentLevelXP);

        JProgressBar xpBar = new JProgressBar(0, 100);
        xpBar.setValue((int)(progress * 100));
        xpBar.setStringPainted(true);
        xpBar.setString(String.format("XP: %,d / %,d", currentXP, nextLevelXP));
        xpBar.setForeground(getSkillColor(skill.getType()));
        panel.add(xpBar);

        return panel;
    }

    private Color getSkillColor(SkillType type) {
        // Combat skills - red
        if (type == SkillType.ATTACK || type == SkillType.STRENGTH ||
            type == SkillType.DEFENSE || type == SkillType.HITPOINTS ||
            type == SkillType.RANGED || type == SkillType.MAGIC) {
            return new Color(200, 50, 50);
        }
        // Gathering skills - green
        if (type == SkillType.MINING || type == SkillType.WOODCUTTING ||
            type == SkillType.FISHING || type == SkillType.FORAGING) {
            return new Color(50, 150, 50);
        }
        // Production skills - blue
        if (type == SkillType.SMITHING || type == SkillType.CRAFTING ||
            type == SkillType.COOKING || type == SkillType.CONSTRUCTION) {
            return new Color(50, 100, 200);
        }
        // Support skills - purple
        return new Color(150, 50, 200);
    }
}
